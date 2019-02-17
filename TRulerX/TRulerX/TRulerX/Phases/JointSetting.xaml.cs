using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

using SkiaSharp;

using TRulerX.Components;
using TRulerX.Phase.PhaseTools;
using TRulerX.Ergonomics;



namespace TRulerX.Phase
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class JointSetting : ContentPage
	{
        private AnalysisSheet sheet;
        private Point[] joints = new Point[7];
        private int jointCursor = 0;
        SKPaint pCircle = new SKPaint
        {
            Style = SKPaintStyle.Fill,
            Color = SKColors.Red,
        };

        //Parent로부터 데이터 공유 가능한 카드(Context)를 넘겨 받는 식으로 고칠 것
		public JointSetting ()
		{
			InitializeComponent ();
            CanvasPlate.PaintSurface += CanvasPlate_PaintSurface;
            sheet = new AnalysisSheet(true);
		}

        public JointSetting(AnalysisSheet sheet)
        {
            InitializeComponent();
            CanvasPlate.PaintSurface += CanvasPlate_PaintSurface;
            this.sheet = sheet;
        }

        public void OnActivated()
        {
            WorkImage.Source = ImageSource.FromStream(() =>
            {
                var stream = sheet.ImageFile.GetStream();
                return stream;
            });
            WorkImage.Rotation = sheet.ImageRotationCorrection;
        }

        private void CanvasPlate_PaintSurface(object sender, SkiaSharp.Views.Forms.SKPaintSurfaceEventArgs e)
        {
            SKImageInfo info = e.Info;
            SKSurface surface = e.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            foreach (Point p in joints)
            {
                canvas.DrawCircle(ConvertToPixel(p), 30, pCircle);
            }
        }

        private void TouchEffect_TouchAction(object sender, TouchActionEventArgs args)
        {
            if(args.Type == TouchActionType.Released)
            {
                OnSelectJoint(args.Location);
            }
        }

        private async void OnSelectJoint(Point p)
        {
            if (jointCursor < 7)
            {
                joints[jointCursor] = p;
                jointCursor++;
                CanvasPlate.InvalidateSurface();
            }
            #region Guide UI Switch
            switch (jointCursor)
            {
                case 0:
                    GuideText.Text = "머리를 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step01.png");
                    break;
                case 1:
                    GuideText.Text = "어깨를 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step02.png");
                    break;
                case 2:
                    GuideText.Text = "팔꿈치를 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step03.png");
                    break;
                case 3:

                    GuideText.Text = "손목을 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step04.png");
                    break;
                case 4:
                    GuideText.Text = "허리를 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step05.png");
                    break;
                case 5:
                    GuideText.Text = "무릎을 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step06.png");
                    break;
                case 6:
                    GuideText.Text = "발을 선택해 주세요";
                    GuideImage.Source = ImageSource.FromFile("step07.png");
                    break;
                case 7:
                    Debug.WriteLine("머리-어깨-팔꿈치" + Skeleton.GetAngle(joints[0], joints[1], joints[2]));
                    Debug.WriteLine("어깨-팔꿈치-손" + Skeleton.GetAngle(joints[1], joints[2], joints[3]));
                    Debug.WriteLine("팔꿈치-어깨-허리" + Skeleton.GetAngle(joints[2], joints[1], joints[4]));
                    Debug.WriteLine("어깨-허리-무릎" + Skeleton.GetAngle(joints[1], joints[4], joints[5]));
                    Debug.WriteLine("허리-무릎-발" + Skeleton.GetAngle(joints[1], joints[4], joints[5]));
                    GuideText.Text = "선택이 완료되었습니다";
                    sheet.JointPoint = joints;
                    GuideImage.Source = null;
                    await Task.Delay(250);
                    ((AnalysisPage)Parent).SetNextReady(PhaseType.JOINT_SETTING);
                    break;
            }
            #endregion
        }

        private SKPoint ConvertToPixel(Point pt)
        {
            return new SKPoint((float)(CanvasPlate.CanvasSize.Width * pt.X / CanvasPlate.Width),
                               (float)(CanvasPlate.CanvasSize.Height * pt.Y / CanvasPlate.Height));
        }

        private void Reset_Button_Clicked(object sender, EventArgs e)
        {
            jointCursor = 0;
            joints = new Point[7];
            GuideText.Text = "머리를 선택해 주세요";
            GuideImage.Source = ImageSource.FromFile("step01.png");
            CanvasPlate.InvalidateSurface();
        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            ((AnalysisPage)Parent).SwitchNext();
        }
    }
}