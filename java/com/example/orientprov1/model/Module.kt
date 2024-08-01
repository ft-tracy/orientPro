package com.example.orientprov1.model

data class Module(
    val moduleId: String,
    val moduleName: String,
    val moduleDetails: List<ModuleDetails>,
    val contents: List<Content>
)
