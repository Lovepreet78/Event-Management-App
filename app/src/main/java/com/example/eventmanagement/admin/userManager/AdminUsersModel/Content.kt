package com.example.eventmanagement.admin.userManager.AdminUsersModel

data class Content(
    val ID: Long,
    val accountExpired: Boolean,
    val accountLocked: Boolean,
    val credentialsExpired: Boolean,
    val roles: List<String>,
    val username: String
)