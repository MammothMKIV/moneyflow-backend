package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.UserProfileDTO
import io.moneyflow.server.entity.User
import io.moneyflow.server.http.request.UserLoginRequest
import io.moneyflow.server.http.request.UserRegistrationRequest
import io.moneyflow.server.http.response.UserLoginResponse
import io.moneyflow.server.mapper.UserMapper
import io.moneyflow.server.response.ApiResponse
import io.moneyflow.server.response.ErrorApiResponse
import io.moneyflow.server.response.SuccessApiResponse
import io.moneyflow.server.service.UserService
import io.moneyflow.server.util.AuthTokenCredentials
import io.moneyflow.server.util.JwtTokenUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import javax.validation.Valid

@Controller
@RequestMapping("user")
class UserController(
    val authenticationManager: AuthenticationManager,
    val userMapper: UserMapper,
    val userService: UserService,
    val jwtTokenUtil: JwtTokenUtil,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun register(@Valid @RequestBody userRegistrationRequest: UserRegistrationRequest) {
        val user = userMapper.map(userRegistrationRequest)

        userService.register(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginRequest: UserLoginRequest): ResponseEntity<out ApiResponse> {
        try {
            val authenticationResult = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userLoginRequest.email,
                    userLoginRequest.password
                )
            )

            val user = authenticationResult.principal as User

            val authTokenCredentials = AuthTokenCredentials(user.id)

            return ResponseEntity<SuccessApiResponse<UserLoginResponse>>(
                SuccessApiResponse(
                    UserLoginResponse(
                        jwtTokenUtil.generateToken(authTokenCredentials),
                        userMapper.map(user)
                    )
                ),
                HttpStatus.OK
            )
        } catch (e: AuthenticationException) {
            return ResponseEntity(ErrorApiResponse("INVALID_CREDENTIALS", "Username or password is incorrect"), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/profile")
    fun getProfile(@AuthenticationPrincipal user: User): ResponseEntity<ApiResponse> {
        return ResponseEntity(SuccessApiResponse(userMapper.map(user)), HttpStatus.OK)
    }

    @PatchMapping("/profile", consumes = ["application/merge-patch+json"])
    fun updateProfile(@RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (patch.has("id")) {
            (patch as ObjectNode).remove("id")
        }

        if (patch.has("createdAt")) {
            (patch as ObjectNode).remove("createdAt")
        }

        val userProfileDTO = userMapper.map(user)
        val original: JsonNode = objectMapper.valueToTree(userProfileDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedUserProfileDTO: UserProfileDTO = objectMapper.treeToValue(patched, UserProfileDTO::class.java) ?: throw RuntimeException("Couldn't convert UserProfileDTO JSON back to UserProfileDTO")

        val validationErrors = BeanPropertyBindingResult(patchedUserProfileDTO, "userProfile")

        validator.validate(patchedUserProfileDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val updatedUser = userMapper.merge(patchedUserProfileDTO, user)

        userService.save(updatedUser)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/password")
    fun changePassword() {
        TODO("Implement change password")
    }

    @PostMapping("/password-reset/request")
    fun passwordResetRequest() {
        TODO("Implement password reset request")
    }

    @PostMapping("/password-reset/confirm")
    fun passwordResetConfirm() {
        TODO("Implement password reset confirmation")
    }
}