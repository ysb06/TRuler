using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Plugin.Media;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Pages
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class PictureSelection : ContentPage
	{
        public event NextEventHandler NextRequested;
        InfoManager infoManager;

		public PictureSelection ()
		{
			InitializeComponent ();
            infoManager = InfoManager.GetInfoManager();
		}

        private void Rotate_Button_Clicked(object sender, EventArgs e)
        {
            TargetImage.Rotation += 90;
            infoManager.PicRotation = (int)TargetImage.Rotation;
        }

        public async void Camera_Button_Clicked(object sender, EventArgs e)
        {
            //*
            if (!CrossMedia.Current.IsCameraAvailable || !CrossMedia.Current.IsTakePhotoSupported)
            {
                await DisplayAlert("No Camera", ":( No camera avaialble.", "OK");
                return;
            }

            var file = await CrossMedia.Current.TakePhotoAsync(new Plugin.Media.Abstractions.StoreCameraMediaOptions
            {
                PhotoSize = Plugin.Media.Abstractions.PhotoSize.Large,
                Directory = "Sample",
                Name = "test.jpg"
            });
            //4MB 이상의 사진은 메모리 문제를 일이키는 것으로 보임
            //Large(75%)옵션으로 문제는 없지만 추후 문제가 생길 경우 처리 방안을 생각할 필요가 있음

            if (file == null)
                return;

            //저장위치: getExternalFilesDir() + Pictures
            //await DisplayAlert("File Location", file.Path, "OK"); //파일 저장 위치 표시
            //*/
            infoManager.PicPath = file.Path;

            //저장된 사진을 다시 불러오기
            TargetImage.Source = ImageSource.FromStream(() =>
            {
                var stream = file.GetStream();
                file.Dispose();
                return stream;
            });
            //이미지 소스를 매니저에 저장할지는 고민해 볼 것
            infoManager.WorkImageSource = TargetImage.Source;
            NextRequested(PhasePage.PICTURE_SELECTION, RequestParam.NONE);
        }

        //

        public async void Gallery_Button_Clicked(object sender, EventArgs e)
        {
            if (!CrossMedia.Current.IsPickPhotoSupported)
            {
                await DisplayAlert("Photos Not Supported", ":( Permission not granted to photos.", "OK");
                return;
            }
            var file = await CrossMedia.Current.PickPhotoAsync(new Plugin.Media.Abstractions.PickMediaOptions
            {
                PhotoSize = Plugin.Media.Abstractions.PhotoSize.Large
            });


            if (file == null)
                return;

            infoManager.PicPath = file.Path;

            TargetImage.Source = ImageSource.FromStream(() =>
            {
                var stream = file.GetStream();
                file.Dispose();
                return stream;
            });
            infoManager.WorkImageSource = TargetImage.Source;
            NextRequested(PhasePage.PICTURE_SELECTION, RequestParam.NONE);
        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            
        }
    }
}