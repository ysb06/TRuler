using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using TRulerX.Ergonomics;

namespace TRulerX.Pages
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class PoseConfirmation : ContentPage
	{
        InfoManager manager;
        private bool IsUpperSelecting = false;
        RiskAnalyzer analyzer;


        public PoseConfirmation ()
		{
			InitializeComponent ();
            Appearing += PoseConfirmation_Appearing;
            manager = InfoManager.GetInfoManager();
		}

        private void PoseConfirmation_Appearing(object sender, EventArgs e)
        {
            Debug.WriteLine("Appeared Pose");
            RawPic.Source = ImageSource.FromFile(manager.PicPath);
            foreach(Point p in manager.JointPoints)
            {
                Debug.WriteLine(p);
            }

            Skeleton skeleton = new Skeleton(manager.JointPoints);
            analyzer = new RiskAnalyzer(skeleton);

            Debug.WriteLine("B: " + skeleton.BackAngle + " -> " + skeleton.BackAngleGroup);
            Debug.WriteLine("S: " + skeleton.ShoulderAngle + " -> " + skeleton.ShoulderAngleGroup);
            Debug.WriteLine("E: " + skeleton.ElbowAngle + " -> " + skeleton.ElbowAngleGroup);
            Debug.WriteLine("A: " + skeleton.AnkleAngle + " -> " + skeleton.AnkleAngleGroup);

            Debug.WriteLine("Upper: " + analyzer.Upper_Posture);
            Debug.WriteLine("Lower: " + analyzer.Lower_Posture);
            
        }

        private void Prev_Button_Clicked(object sender, EventArgs e)
        {
            if(IsUpperSelecting)
            {
                analyzer.SwitchToPrev(PostureType.Upper);
                PosePic.Source = ImageSource.FromFile(analyzer.GetUpperPosturePath());
            }
            else
            {
                analyzer.SwitchToPrev(PostureType.Lower);
                PosePic.Source = ImageSource.FromFile(analyzer.GetCompletePosturePath());
            }
        }

        private void Next_Button_Clicked(object sender, EventArgs e)
        {
            if (IsUpperSelecting)
            {
                analyzer.SwitchToNext(PostureType.Upper);
                PosePic.Source = ImageSource.FromFile(analyzer.GetUpperPosturePath());
            }
            else
            {
                analyzer.SwitchToNext(PostureType.Lower);
                PosePic.Source = ImageSource.FromFile(analyzer.GetCompletePosturePath());
            }
        }

        private void Yes_Button_Clicked(object sender, EventArgs e)
        {
            if(IsUpperSelecting)
            {
                IsUpperSelecting = false;
            }
            else
            {
                //Go next!!
            }
        }
    }
}