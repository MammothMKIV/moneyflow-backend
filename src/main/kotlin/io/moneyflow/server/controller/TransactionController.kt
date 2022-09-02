package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.TransactionDTO
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.entity.User
import io.moneyflow.server.mapper.TransactionMapper
import io.moneyflow.server.service.AccessControlService
import io.moneyflow.server.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/transactions")
class TransactionController(
    val transactionService: TransactionService,
    val transactionMapper: TransactionMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator,
    val accessControlService: AccessControlService
) {
    @PostMapping("")
    fun create(@Valid @RequestBody transactionDTO: TransactionDTO, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val transaction = transactionMapper.map(transactionDTO)

        if (accessControlService.canManageTransactionHouseholds(user, transaction)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        transactionService.save(transaction)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable("id") transaction: Transaction, @RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (accessControlService.canManageTransactionHouseholds(user, transaction)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val transactionDTO = transactionMapper.map(transaction)
        val original: JsonNode = objectMapper.valueToTree(transactionDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedTransactionDTO: TransactionDTO = objectMapper.treeToValue(patched, TransactionDTO::class.java) ?: throw RuntimeException("Couldn't convert Transaction JSON back to Transaction")

        val validationErrors = BeanPropertyBindingResult(patchedTransactionDTO, "transaction")

        validator.validate(patchedTransactionDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val patchedTransaction = transactionMapper.merge(patchedTransactionDTO, transaction)

        if (accessControlService.canManageTransactionHouseholds(user, patchedTransaction)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        transactionService.save(patchedTransaction)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable("id") transaction: Transaction, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (accessControlService.canManageTransactionHouseholds(user, transaction)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        transactionService.delete(transaction.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}