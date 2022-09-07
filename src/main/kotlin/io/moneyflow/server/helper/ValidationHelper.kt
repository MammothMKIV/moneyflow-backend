package io.moneyflow.server.helper

import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import java.util.HashMap

class ValidationHelper {
    companion object {
        fun bindingResultToErrorMap(bindingResult: BindingResult): Map<String, String> {
            val errors = HashMap<String, String>()

            bindingResult.allErrors.forEach {
                if (it is FieldError) {
                    val fieldName = it.field
                    val errorMessage = it.defaultMessage!!

                    errors[fieldName] = errorMessage
                } else {
                    errors["[ROOT]"] = it.defaultMessage!!
                }
            }

            return errors
        }
    }
}