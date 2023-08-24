package com.curenosm.chapter8;

import java.io.IOException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.nio.file.*;
import ai.djl.inference.*;
import ai.djl.modality.*;
import ai.djl.modality.cv.*;
import ai.djl.modality.cv.transform.*;
import ai.djl.modality.cv.translator.*;
import ai.djl.repository.zoo.*;
import ai.djl.translate.*;
import ai.djl.training.util.*;

@Configuration
public class ConfigPyTorch {

  @Bean
  @Order(9)
  public ApplicationRunner loadModelPytorch() {
    return args -> {
      DownloadUtils.download(
        "https://djl-ai.s3.amazonaws.com/mlrepo/model/cv"
          + "/image_classification/ai/djl/pytorch/resnet/0.0.1/traced_resnet18.pt.gz",
        "build/pytorch_models/resnet18/resnet18.pt",
        new ProgressBar());

      DownloadUtils.download(
        "https://djl-ai.s3.amazonaws.com/mlrepo/model/cv/"
          + "image_classification/ai/djl/pytorch/synset.txt",
        "build/pytorch_models/resnet18/synset.txt",
        new ProgressBar());

      Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
        .addTransform(new Resize(256))
        .addTransform(new CenterCrop(224, 224))
        .addTransform(new ToTensor())
        .addTransform(new Normalize(
          new float[] {0.485f, 0.456f, 0.406f},
          new float[] {0.229f, 0.224f, 0.225f}))
        .optApplySoftmax(true)
        .build();

      Criteria<Image, Classifications> criteria = Criteria
        .builder()
        .setTypes(Image.class, Classifications.class)
        .optModelPath(Paths.get("build/pytorch_models/resnet18"))
        .optOption("mapLocation", "true") // this model requires mapLocation for GPU
        .optTranslator(translator)
        .optProgress(new ProgressBar()).build();

      try (ZooModel<Image, Classifications> model = criteria.loadModel()){
        var img = ImageFactory.getInstance().fromUrl(
          "https://raw.githubusercontent.com/pytorch/hub/master/images/dog.jpg"
        );

        img.getWrappedImage();

        Predictor<Image, Classifications> predictor = model.newPredictor();
        Classifications classifications = predictor.predict(img);

        System.out.println(classifications);
      } catch (IOException e) {
        e.printStackTrace();
      }

    };
  }

}
