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
        public event EventHandler RequestNextPage;

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

        private async void Camera_Button_Clicked(object sender, EventArgs e)
        {
            //*
            if (!CrossMedia.Current.IsCameraAvailable || !CrossMedia.Current.IsTakePhotoSupported)
            {
                await DisplayAlert("No Camera", ":( No camera avaialble.", "OK");
                return;
            }

            var file = await CrossMedia.Current.TakePhotoAsync(new Plugin.Media.Abstractions.StoreCameraMediaOptions
            {
                PhotoSize = Plugin.Media.Abstractions.PhotoSize.Medium,
                Directory = "Sample",
                Name = "test.jpg"
            });

            if (file == null)
                return;

            //저장위치: getExternalFilesDir() + Pictures
            //await DisplayAlert("File Location", file.Path, "OK"); //파일 저장 위치 표시
            //*/
            infoManager.PicPath = file.Path;

            //저장된 사진을 다시 불러오기
            TargetImage.Source = ImageSource.FromFile(file.Path);
        }

        private void Gallery_Button_Clicked(object sender, EventArgs e)
        {

        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            RequestNextPage(this, new EventArgs());
        }
    }
}