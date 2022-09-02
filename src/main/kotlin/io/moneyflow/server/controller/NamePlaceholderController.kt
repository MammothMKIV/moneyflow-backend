package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.NamePlaceholderDTO
import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.NamePlaceholder
import io.moneyflow.server.entity.User
import io.moneyflow.server.mapper.NamePlaceholderMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.AccessControlService
import io.moneyflow.server.service.NamePlaceholderService
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
@RequestMapping("/name-placeholders")
class NamePlaceholderController(
    val namePlaceholderService: NamePlaceholderService,
    val namePlaceholderMapper: NamePlaceholderMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator,
    val accessControlService: AccessControlService
) {
    @PostMapping("")
    fun create(@Valid @RequestBody namePlaceholderDTO: NamePlaceholderDTO, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val namePlaceholder = namePlaceholderMapper.map(namePlaceholderDTO)

        if (!accessControlService.canManageHousehold(user, namePlaceholder.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        namePlaceholderService.save(namePlaceholder)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("")
    fun getList(@RequestParam(required = true, name = "household") household: Household, @AuthenticationPrincipal user: User): ResponseEntity<ListApiResponse<NamePlaceholderDTO>> {
        if (!accessControlService.canManageHousehold(user, household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val namePlaceholders = namePlaceholderService.getByHousehold(household)

        return if (namePlaceholders.isNotEmpty()) {
            ResponseEntity(ListApiResponse(namePlaceholders.toList().map(namePlaceholderMapper::map), namePlaceholders.size.toLong()), HttpStatus.OK)
        } else {
            ResponseEntity(ListApiResponse(Collections.emptyList(), 0), HttpStatus.OK)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable("id") namePlaceholder: NamePlaceholder, @RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, namePlaceholder.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
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

        val patchedNamePlaceholder = namePlaceholderMapper.merge(patchedNamePlaceholderDTO, namePlaceholder)

        namePlaceholderService.save(patchedNamePlaceholder)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable("id") namePlaceholder: NamePlaceholder, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, namePlaceholder.household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        namePlaceholderService.delete(namePlaceholder.id)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}