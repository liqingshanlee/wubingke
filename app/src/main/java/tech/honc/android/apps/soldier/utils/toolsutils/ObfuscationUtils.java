package tech.honc.android.apps.soldier.utils.toolsutils;

import android.net.Uri;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by MrJiang on 2016/5/11.
 */
public final class ObfuscationUtils {

  /**
   * Set image
   */
  public static void setSimpleDraweeView(SimpleDraweeView simpleDraweeView,
      Postprocessor postprocessor, Uri uri) {
    ImageRequest request =
        ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(postprocessor).build();

    PipelineDraweeController controller =
        (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(simpleDraweeView.getController())
            .build();
    simpleDraweeView.setController(controller);
  }
}
