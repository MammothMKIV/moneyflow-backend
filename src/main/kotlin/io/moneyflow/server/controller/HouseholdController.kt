package io.moneyflow.server.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import io.moneyflow.server.dto.HouseholdDTO
import io.moneyflow.server.entity.Household
import io.moneyflow.server.entity.User
import io.moneyflow.server.mapper.HouseholdMapper
import io.moneyflow.server.response.ListApiResponse
import io.moneyflow.server.service.AccessControlService
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
@RequestMapping("/households")
class HouseholdController(
    val householdService: HouseholdService,
    val householdMapper: HouseholdMapper,
    val objectMapper: ObjectMapper,
    val validator: SmartValidator,
    val accessControlService: AccessControlService
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun create(@Valid @RequestBody householdDTO: HouseholdDTO, @AuthenticationPrincipal user: User) {
        val household = householdMapper.map(householdDTO)

        household.owner = user

        householdService.save(household)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun getList(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "20") perPage: Int, @AuthenticationPrincipal user: User): ListApiResponse<HouseholdDTO> {
        val households = householdService.getAllOwnedBy(page, perPage, user)

        return if (!households.isEmpty) {
            ListApiResponse(households.toList().map(householdMapper::map), households.totalElements)
        } else {
            ListApiResponse(Collections.emptyList(), 0)
        }
    }

    @PatchMapping(path = ["{id}"], consumes = ["application/merge-patch+json"])
    fun update(@PathVariable("id") household: Household, @RequestBody patch: JsonNode, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val householdDTO = householdMapper.map(household)
        val original: JsonNode = objectMapper.valueToTree(householdDTO)
        val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
        val patchedHouseholdDTO: HouseholdDTO = objectMapper.treeToValue(patched, HouseholdDTO::class.java) ?: throw RuntimeException("Couldn't convert HouseholdDTO JSON back to HouseholdDTO")

        val validationErrors = BeanPropertyBindingResult(patchedHouseholdDTO, "household")

        validator.validate(patchedHouseholdDTO, validationErrors)

        if (validationErrors.hasErrors()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val patchedHousehold = householdMapper.merge(patchedHouseholdDTO, household)

        householdService.save(patchedHousehold)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping(path = ["{id}"])
    fun delete(@PathVariable("id") household: Household, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        if (!accessControlService.canManageHousehold(user, household)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        householdService.delete(household.id!!)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}