package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.UserProfileDTO
import io.moneyflow.server.entity.OperationConfirmationType
import io.moneyflow.server.entity.User
import io.moneyflow.server.http.request.UserLoginRequest
import io.moneyflow.server.http.request.UserPasswordResetConfirmRequest
import io.moneyflow.server.http.request.UserPasswordResetRequest
import io.moneyflow.server.http.request.UserPasswordUpdateRequest
import io.moneyflow.server.http.request.UserRegistrationRequest
import io.moneyflow.server.http.response.UserLoginResponse
import io.moneyflow.server.mapper.UserMapper
import io.moneyflow.server.response.ApiResponse
import io.moneyflow.server.response.ErrorApiResponse
import io.moneyflow.server.response.SuccessApiResponse
import io.moneyflow.server.service.OperationConfirmationService
import io.moneyflow.server.service.UserService
import io.moneyflow.server.util.AuthTokenCredentials
import io.moneyflow.server.util.JwtTokenUtil
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.IllegalArgumentException
import javax.validation.Valid

@Controller
@RequestMapping("user")
class UserController(
    val authenticationManager: AuthenticationManager,
    val userMapper: UserMapper,
    val userService: UserService,
    val jwtTokenUtil: JwtTokenUtil,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator,
    val operationConfirmationService: OperationConfirmationService
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
            return ResponseEntity(ErrorApiResponse("INVALID_CREDENTIALS", "Username or password is incorrect", null), HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/profile")
    fun getProfile(@AuthenticationPrincipal user: User): ResponseEntity<ApiResponse> {
        return ResponseEntity(SuccessApiResponse(userMapper.map(user)), HttpStatus.OK)
    }

    @PatchMapping("/profile", consumes = ["application/merge-patch+json"])
    fun updateProfile(@RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val userProfileDTO = userMapper.map(user)
        val original: JsonNode = objectMapper.valueToTree(userProfileDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedUserProfileDTO: UserProfileDTO = objectMapper.treeToValue(patched, UserProfileDTO::class.java) ?: throw RuntimeException("Couldn't convert UserProfileDTO JSON back to UserProfileDTO")

        val validationErrors = BeanPropertyBindingResult(patchedUserProfileDTO, "userProfile")

        validator.validate(patchedUserProfileDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            throw MethodArgumentNotValidException(MethodParameter(this.javaClass.getDeclaredMethod("updateProfile", JsonNode::class.java, User::class.java), 0), validationErrors)
        }

        val updatedUser = userMapper.merge(patchedUserProfileDTO, user)

        userService.save(updatedUser)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/password")
    fun changePassword(@RequestBody userPasswordUpdateRequest: UserPasswordUpdateRequest, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        try {
            userService.updatePassword(user, userPasswordUpdateRequest.oldPassword, userPasswordUpdateRequest.newPassword)
        } catch (e: BadCredentialsException) {
            return ResponseEntity(ErrorApiResponse("INVALID_CREDENTIALS", e.message, null), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/password-reset/request")
    fun passwordResetRequest(@Valid @RequestBody passwordResetRequest: UserPasswordResetRequest): ResponseEntity<ApiResponse> {
        try {
            val user = userService.loadUserByUsername(passwordResetRequest.email) as User
            val confirmationToken = operationConfirmationService.issue(user, OperationConfirmationType.USER_PASSWORD_RESET, 60 * 60)
            println(operationConfirmationService.encodeToken(confirmationToken))
        } catch (e: UsernameNotFoundException) {
            return ResponseEntity(ErrorApiResponse("USER_NOT_FOUND", e.message, null), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/password-reset/confirm")
    fun passwordResetConfirm(@Valid @RequestBody passwordResetConfirmRequest: UserPasswordResetConfirmRequest): ResponseEntity<ApiResponse> {
        try {
            val token = operationConfirmationService.decodeToken(passwordResetConfirmRequest.token)
            val operationConfirmation = operationConfirmationService.get(token, OperationConfirmationType.USER_PASSWORD_RESET)
                ?: return ResponseEntity(ErrorApiResponse("INVALID_TOKEN", "Invalid token", null), HttpStatus.BAD_REQUEST)

            operationConfirmationService.verify(operationConfirmation, token)

            userService.updatePassword(operationConfirmation.user!!, passwordResetConfirmRequest.password)

            operationConfirmationService.purge(operationConfirmation)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(ErrorApiResponse("INVALID_TOKEN", e.message, null), HttpStatus.BAD_REQUEST)
        } catch (e: BadCredentialsException) {
            return ResponseEntity(ErrorApiResponse("INVALID_TOKEN", e.message, null), HttpStatus.BAD_REQUEST)
        } catch (e: CredentialsExpiredException) {
            return ResponseEntity(ErrorApiResponse("TOKEN_EXPIRED", e.message, null), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}