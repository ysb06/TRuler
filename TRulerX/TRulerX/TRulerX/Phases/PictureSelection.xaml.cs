using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Plugin.Media;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

using TRulerX.Ergonomics;
using TRulerX.Phase.PhaseTools;

namespace TRulerX.Phase
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class PictureSelection : ContentPage
	{
        private AnalysisSheet sheet;

        public PictureSelection()
        {
            InitializeComponent();
            sheet = new AnalysisSheet(true);
        }

        public PictureSelection(AnalysisSheet sheet)
        {
            InitializeComponent();
            this.sheet = sheet;
        }

        private void Rotate_Button_Clicked(object sender, EventArgs e)
        {
            TargetImage.Rotation += 90;
            sheet.ImageRotationCorrection = TargetImage.Rotation;
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
                PhotoSize = Plugin.Media.Abstractions.PhotoSize.Medium,
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
            sheet.ImagePath = file.Path;
            sheet.ImageFile = file;

            //저장된 사진을 다시 불러오기
            TargetImage.Source = ImageSource.FromStream(() =>
            {
                var stream = file.GetStream();
                return stream;
            });
            ((AnalysisPage)Parent).SetNextReady(PhaseType.PICTURE_SELECTION);
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

            sheet.ImagePath = file.Path;
            sheet.ImageFile = file;

            TargetImage.Source = ImageSource.FromStream(() =>
            {
                var stream = file.GetStream();
                return stream;
            });
            ((AnalysisPage)Parent).SetNextReady(PhaseType.PICTURE_SELECTION);
        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            ((AnalysisPage)Parent).SwitchNext();
        }
    }
}