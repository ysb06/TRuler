using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using TRulerX.Ergonomics;
using TRulerX.Phase.PhaseTools;

namespace TRulerX.Phase
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class PoseConfirmation : ContentPage
	{
        private AnalysisSheet sheet;
        private bool IsUpperSelecting = true;
        private PostureAnalyzer poseAnalyzer;

        private UpperPosture upperPosture = UpperPosture.B0_S0_E45;
        private LowerPosture lowerPosture = LowerPosture.STD;

        public PoseConfirmation()
		{
			InitializeComponent();
            sheet = new AnalysisSheet(true);
		}

        public PoseConfirmation(AnalysisSheet sheet)
        {
            InitializeComponent();
            this.sheet = sheet;
        }

        private void Initialize()
        {
            Skeleton skeleton = new Skeleton(sheet.JointPoint);
            poseAnalyzer = new PostureAnalyzer(skeleton);
            IsUpperSelecting = true;

            //RiskAnalysis에 의한 자세 인식 및 선택 코드 추가
            upperPosture = poseAnalyzer.Upper_Posture;
            lowerPosture = poseAnalyzer.Lower_Posture;

            //원본 소스 표시
            RawPic.Source = ImageSource.FromStream(() =>
            {
                var stream = sheet.ImageFile.GetStream();
                return stream;
            });
            RawPic.Rotation = sheet.ImageRotationCorrection;

            //자세 소스 표시

            PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetUpperPoseImageFileName(upperPosture));
        }        

        public void OnActivated()
        {
            Initialize();
        }

        private void Prev_Button_Clicked(object sender, EventArgs e)
        {
            if (IsUpperSelecting)
            {
                int cursor = (int)upperPosture - 1;
                if(cursor < 0)
                {
                    cursor = 13;
                }
                upperPosture = (UpperPosture)cursor;
                PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetUpperPoseImageFileName(upperPosture));
            }
            else
            {
                int cursor = (int)lowerPosture - 1;
                if (cursor < 0)
                {
                    cursor = 12;
                }
                lowerPosture = (LowerPosture)cursor;
                PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetFullPoseImageFileName(upperPosture, lowerPosture));
            }

        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            if (IsUpperSelecting)
            {
                int cursor = (int)upperPosture + 1;
                if (cursor > 13)
                {
                    cursor = 0;
                }
                upperPosture = (UpperPosture)cursor;
                PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetUpperPoseImageFileName(upperPosture));
            }
            else
            {
                int cursor = (int)lowerPosture + 1;
                if (cursor > 12)
                {
                    cursor = 0;
                }
                lowerPosture = (LowerPosture)cursor;
                PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetFullPoseImageFileName(upperPosture, lowerPosture));
            }
        }

        private void Yes_Button_Clicked(object sender, EventArgs e)
        {
            if(IsUpperSelecting)
            {
                IsUpperSelecting = false;
                PosePic.Source = ImageSource.FromFile(PostureAnalyzer.GetFullPoseImageFileName(upperPosture, lowerPosture));
                PosePartGuide.Text = "하지";
                sheet.Upper = upperPosture;
            }
            else
            {
                sheet.Lower = lowerPosture;
                //RiskAnalysis에 의한 위험도 계산

                AnalysisPage parent = (AnalysisPage)Parent;
                parent.SetNextReady(PhaseType.POSE_CONFIRMATION);
                parent.SwitchNext();
            }
        }
    }
}