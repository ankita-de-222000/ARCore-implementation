package com.applex.arcore1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isadded=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomArfragment customArfragment=(CustomArfragment)getSupportFragmentManager().findFragmentById(R.id.arfragment);

        ModelRenderable.builder().setSource(this,R.raw.fox_face).build()
                .thenAccept(renderable->{
                    modelRenderable=renderable;
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(false);
                });
        Texture.builder().setSource(this,R.drawable.fox_face_mesh_texture).build()
                .thenAccept(texture1 -> this.texture=texture1);
        customArfragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customArfragment.getArSceneView().getScene().addOnUpdateListener(frameTime ->{
            if(modelRenderable==null || texture==null)
                return;

            Frame frame=customArfragment.getArSceneView().getArFrame();
            Collection<AugmentedFace> augmentedFaces=frame.getUpdatedTrackables(AugmentedFace.class);
            for(AugmentedFace augmentedFace:augmentedFaces){
                if(isadded)
                    return;
                AugmentedFaceNode augmentedFaceNode=new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArfragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);
                isadded=true;
            }

        });
    }
}
