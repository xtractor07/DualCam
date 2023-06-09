package com.example.dualcam

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.util.Size
import android.view.TextureView

object CameraUtils {

    /** Return the biggest preview size available which is smaller than the window */
    private fun findBestPreviewSize(windowSize: Size, characteristics: CameraCharacteristics):
        Size {
            val supportedPreviewSizes: List<Size> =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    ?.getOutputSizes(SurfaceTexture::class.java)
                    ?.filter { SizeComparator.compare(it, windowSize) >= 0 }
                    ?.sortedWith(SizeComparator)
                    ?: emptyList()

            return supportedPreviewSizes.getOrElse(0) { Size(0, 0) }
        }

    /**
     * Returns a new SurfaceTexture that will be the target for the camera preview
     */
    fun buildTargetTexture(
        containerView: TextureView,
        characteristics: CameraCharacteristics
    ): SurfaceTexture? {

        /*** Codelab --> Change this function to handle viewfinder rotation and scaling ***/

        val windowSize = Size(containerView.width, containerView.height)
        val previewSize = findBestPreviewSize(windowSize, characteristics)

        return containerView.surfaceTexture?.apply {
            setDefaultBufferSize(previewSize.width, previewSize.height)
        }
    }
}

internal object SizeComparator : Comparator<Size> {
    override fun compare(a: Size, b: Size): Int {
        return b.height * b.width - a.width * a.height
    }
}
