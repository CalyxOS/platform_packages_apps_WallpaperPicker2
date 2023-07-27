/*
 * Copyright (C) 2023 The Android Open Source Project
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
package com.android.wallpaper.picker.wallpaper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import com.android.wallpaper.R
import com.android.wallpaper.model.ImageWallpaperInfo
import com.android.wallpaper.model.WallpaperInfo
import com.android.wallpaper.picker.AppbarFragment
import com.android.wallpaper.picker.BasePreviewActivity
import com.android.wallpaper.util.ActivityUtils
import dagger.hilt.android.AndroidEntryPoint

/** This activity holds the flow for the preview screen. */
@AndroidEntryPoint(BasePreviewActivity::class)
class WallpaperPreviewSectionActivity :
    Hilt_WallpaperPreviewSectionActivity(), AppbarFragment.AppbarFragmentHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFullScreen = intent.getBooleanExtra(EXTRA_IS_FULL_SCREEN, false)
        if (isFullScreen) {
            window.allowEnterTransitionOverlap = true
            window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            window.exitTransition = Slide()
            window.enterTransition = Slide()
            setContentView(R.layout.activity_preview)
        }
        // TODO: @abdullahirum create new layout and use here
        setContentView(R.layout.activity_preview)
        enableFullScreen()
        // TODO: @abdullahirum navigate to appropriate screen here
    }

    override fun onUpArrowPressed() {
        onBackPressed()
    }

    override fun isUpArrowSupported(): Boolean {
        return !ActivityUtils.isSUWMode(baseContext)
    }

    // TODO(b/292592383): migrate activity result method to latest api callback
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_MY_PHOTOS && resultCode == RESULT_OK) {
            val imageUri = data?.let { it.data as? Uri }
            if (imageUri != null) {
                val imageWallpaper = ImageWallpaperInfo(imageUri)
                // TODO: @abullahirum navigate to preview screen
            }
        }
    }

    companion object {
        /** Request code to map activity result for ImageWallpaperInfo */
        val RESULT_MY_PHOTOS = 0
        const val EXTRA_IS_FULL_SCREEN = "com.android.wallpaper.picker.is_full_screen"

        /** Returns a new Intent with the provided WallpaperInfo instance put as an extra. */
        fun newIntent(
            packageContext: Context?,
            wallpaperInfo: WallpaperInfo?,
            isFullScreen: Boolean = false
        ): Intent {
            val intent = Intent(packageContext, WallpaperPreviewSectionActivity::class.java)
            if (isFullScreen) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            intent.putExtra(EXTRA_WALLPAPER_INFO, wallpaperInfo)
            intent.putExtra(EXTRA_IS_FULL_SCREEN, wallpaperInfo)
            return intent
        }
    }
}
