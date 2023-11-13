package com.example.eventmanagement.admin.userManager.AdminUsersModel

data class Content(
    val id: Long,
    val accountExpired: Boolean,
    val accountLocked: Boolean,
    val credentialsExpired: Boolean,
    val roles: List<String>,
    val username: String
)