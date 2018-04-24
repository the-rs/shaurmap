package com.rightsoftware.shaurmap.data.device

import android.content.Context
import com.rightsoftware.shaurmap.business.repository.IDeviceSettingsRepository
import com.rightsoftware.shaurmap.data.utils.extensions.isLocationEnabled
import javax.inject.Inject

class DeviceSettingsRepository @Inject constructor(
        private val context: Context
) : IDeviceSettingsRepository {
    override fun isLocationEnabled() = context.isLocationEnabled()
}