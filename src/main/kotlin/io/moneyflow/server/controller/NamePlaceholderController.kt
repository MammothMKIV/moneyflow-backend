package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.NamePlaceholderDTO
import io.moneyflow.server.mapper.NamePlaceholderMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.NamePlaceholderService
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
@RequestMapping("/name-placeholders")
class NamePlaceholderController(
    val namePlaceholderService: NamePlaceholderService,
    val namePlaceholderMapper: NamePlaceholderMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody namePlaceholderDTO: NamePlaceholderDTO) {
        val namePlaceholder = namePlaceholderMapper.map(namePlaceholderDTO)

        namePlaceholderService.save(namePlaceholder)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int): ListApiResponse<NamePlaceholderDTO> {
        val namePlaceholders = namePlaceholderService.getAll(page, perPage)

        return if (!namePlaceholders.isEmpty) {
            ListApiResponse(namePlaceholders.toList().map(namePlaceholderMapper::map), namePlaceholders.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable id: Long, @RequestBody patch: JsonNode): ResponseEntity<Any> {
        var namePlaceholder = namePlaceholderService.get(id)

        if (namePlaceholder == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val namePlaceholderDTO = namePlaceholderMapper.map(namePlaceholder)
        val original: JsonNode = objectMapper.valueToTree(namePlaceholderDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedNamePlaceholderDTO: NamePlaceholderDTO = objectMapper.treeToValue(patched, NamePlaceholderDTO::class.java) ?: throw RuntimeException("Couldn't convert NamePlaceholderDTO JSON back to NamePlaceholderDTO")

        val validationErrors = BeanPropertyBindingResult(patchedNamePlaceholderDTO, "namePlaceholder")

        validator.validate(patchedNamePlaceholderDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        namePlaceholder = namePlaceholderMapper.merge(patchedNamePlaceholderDTO, namePlaceholder)

        namePlaceholderService.save(namePlaceholder)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val namePlaceholder = namePlaceholderService.get(id)

        if (namePlaceholder == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        namePlaceholderService.delete(namePlaceholder.id)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}