package com.rightsoftware.shaurmap.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class Restaurant(
        @Index val id: String,
        val latitude : Double,
        val longitude : Double,
        @Id var localId: Long = 0)
