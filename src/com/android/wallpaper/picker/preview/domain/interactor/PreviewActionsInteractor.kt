/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wallpaper.picker.preview.domain.interactor

import com.android.wallpaper.model.wallpaper.WallpaperModel
import com.android.wallpaper.picker.preview.data.repository.WallpaperPreviewRepository
import com.android.wallpaper.picker.preview.shared.model.LiveWallpaperDownloadResultModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** This class handles the business logic for Preview screen's action buttons */
@ActivityRetainedScoped
class PreviewActionsInteractor
@Inject
constructor(
    private val wallpaperPreviewRepository: WallpaperPreviewRepository,
) {
    val wallpaperModel: StateFlow<WallpaperModel?> = wallpaperPreviewRepository.wallpaperModel

    private val _isDownloadingWallpaper = MutableStateFlow<Boolean>(false)
    val isDownloadingWallpaper: Flow<Boolean> = _isDownloadingWallpaper.asStateFlow()

    suspend fun downloadWallpaper(): LiveWallpaperDownloadResultModel? {
        _isDownloadingWallpaper.value = true
        val wallpaperModel = wallpaperPreviewRepository.downloadWallpaper()
        _isDownloadingWallpaper.value = false
        return wallpaperModel
    }
}
