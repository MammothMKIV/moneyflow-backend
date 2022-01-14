package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.TransactionDTO
import io.moneyflow.server.entity.Transaction
import io.moneyflow.server.mapper.TransactionMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
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
@RequestMapping("/transactions")
class TransactionController(
    val transactionService: TransactionService,
    val transactionMapper: TransactionMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody transactionDTO: TransactionDTO) {
        val transaction = transactionMapper.map(transactionDTO)

        transactionService.save(transaction)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int): ListApiResponse<TransactionDTO> {
        val transactions = transactionService.getAll(page, perPage)

        return if (!transactions.isEmpty) {
            ListApiResponse(transactions.toList().map(transactionMapper::map), transactions.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun getList(@PathVariable id: Long, @RequestBody patch: JsonNode): ResponseEntity<Any> {
        val transaction = transactionService.get(id)

        if (patch.has("id")) {
            (patch as ObjectNode).remove("id")
        }

        if (transaction == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val original: JsonNode = objectMapper.valueToTree(transaction)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedTransaction: Transaction = objectMapper.treeToValue(patched, Transaction::class.java) ?: throw RuntimeException("Couldn't convert Transaction JSON back to Transaction")

        val validationErrors = BeanPropertyBindingResult(patchedTransaction, "transaction")

        validator.validate(patchedTransaction, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        transactionService.save(patchedTransaction)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}