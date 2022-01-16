package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.AccountDTO
import io.moneyflow.server.entity.Account
import io.moneyflow.server.mapper.AccountMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    val accountMapper: AccountMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody accountDTO: AccountDTO) {
        val account = accountMapper.map(accountDTO)

        accountService.save(account)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int): ListApiResponse<AccountDTO> {
        val accounts = accountService.getAll(page, perPage)

        return if (!accounts.isEmpty) {
            ListApiResponse(accounts.toList().map(accountMapper::map), accounts.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable id: Long, @RequestBody patch: JsonNode): ResponseEntity<Any> {
        val account = accountService.get(id)

        if (patch.has("id")) {
            (patch as ObjectNode).remove("id")
        }

        if (account == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val original: JsonNode = objectMapper.valueToTree(account)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedAccount: Account = objectMapper.treeToValue(patched, Account::class.java) ?: throw RuntimeException("Couldn't convert Account JSON back to Account")

        val validationErrors = BeanPropertyBindingResult(patchedAccount, "account")

        validator.validate(patchedAccount, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        accountService.save(patchedAccount)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val account = accountService.get(id)

        if (account == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        accountService.delete(account.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}