package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.AccountDTO
import io.moneyflow.server.entity.Account
import io.moneyflow.server.entity.User
import io.moneyflow.server.mapper.AccountMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.AccessControlService
import io.moneyflow.server.service.AccountService
import io.moneyflow.server.service.HouseholdService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountService: AccountService,
    val accessControlService: AccessControlService,
    val householdService: HouseholdService,
    val accountMapper: AccountMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody accountDTO: AccountDTO, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val account = accountMapper.map(accountDTO)

        if (!accessControlService.canCreateAccountsInHousehold(user, account.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        accountService.save(account)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int, @AuthenticationPrincipal user: User): ListApiResponse<AccountDTO> {
        val accounts = accountService.getAllByHouseholds(page, perPage, householdService.getAllOwnedBy(user))

        return if (!accounts.isEmpty) {
            ListApiResponse(accounts.toList().map(accountMapper::map), accounts.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable("id") account: Account, @RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canEditAccount(user, account)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val accountDTO = accountMapper.map(account)
        val original: JsonNode = objectMapper.valueToTree(accountDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedAccountDTO: AccountDTO = objectMapper.treeToValue(patched, AccountDTO::class.java) ?: throw RuntimeException("Couldn't convert AccountDTO JSON back to AccountDTO")

        val validationErrors = BeanPropertyBindingResult(patchedAccountDTO, "account")

        validator.validate(patchedAccountDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val patchedAccount = accountMapper.merge(patchedAccountDTO, account)

        accountService.save(patchedAccount)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable("id") account: Account, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canDeleteAccount(user, account)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        accountService.delete(account.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}