package com.rudraksh.naamjaap.domain.usecase

import javax.inject.Inject

class ValidateCustomTargetUseCase @Inject constructor() {
    operator fun invoke(input: String): Result<Int> {
        if (input.isBlank()) {
            return Result.failure(IllegalArgumentException("Target cannot be empty"))
        }

        val target = input.toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Target must be a valid number"))

        if (target !in 1..100000) {
            return Result.failure(IllegalArgumentException("Target must be between 1 and 100,000"))
        }

        return Result.success(target)
    }
}
