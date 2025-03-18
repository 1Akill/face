<template>
  <div >
    <div>
      <h2>门禁</h2>
      <div>
        <div >
          <video id="video"></video>
        </div>
        <div id="overflow">

        </div>
        <div style="display: none;">
          <canvas id="canvas"></canvas>
        </div>
      </div>
    </div>
    <el-button type="primary" @click="onClick">拍照</el-button>
  </div>
</template>

<script>

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name:"Face",
  async mounted() {
    this.startCamera()
  },
  methods: {
    onClick(){
      this.startFaceLoginInterval()
    },
    startCamera() {
      const video = document.getElementById('video');

      if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        return navigator.mediaDevices.getUserMedia({ video: true })
            .then(stream => {
              video.srcObject = stream;
              return new Promise(resolve => video.onloadedmetadata = resolve);
            })
            .then(() => {
              if (video.readyState >= 4) {
                video.play();
              } else {
                video.oncanplay = () => video.play();
              }
            })
            .catch(error => {
              console.log('摄像头无法打开', error);
            });
      }
    },
    captureImage() {
      return new Promise(resolve => {
        const video = document.getElementById('video');
        const canvas = document.createElement('canvas');

        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;

        const context = canvas.getContext('2d');
        context.drawImage(video, 0, 0, canvas.width, canvas.height);

        resolve(canvas.toDataURL('image/png'));
      });
    },

    async captureAndLogImage() {
      await this.startCamera();
      let image = await this.captureImage();
      image = image.split(',')[1]
      return image
    },
    async startFaceLoginInterval() {
      const image = await this.captureAndLogImage();
      console.log(image)
      const data = { image: image };

      this.$axios.post(this.$api.http+'/user-info/face-recognition', data, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
          .then(response => {
            console.log("返回",response)
            if (response.data.code === 0 ) {
              let userInfo = response.data.data;
              this.$message.success(userInfo.name+",出入平安")
            } else {
              this.$message.error(response.data.msg)
            }
          })
          .catch(error => {
            console.log('登录接口调用失败', error);
          });


      // const intervalId = setInterval(async () => {
      //   debugger
      //
      // }, 5000);
    },
    stopCamera() {
      const video = document.getElementById('video');
      const stream = video.srcObject;
      console.log("stream->",stream)
      if (stream && stream.getTracks) {
        stream.getTracks().forEach(track => track.stop());
        video.srcObject = null;
      }
    }


  }
};
</script>

<style scoped>

</style>