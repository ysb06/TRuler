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
            RiskAnalyzer analyzer = new RiskAnalyzer(skeleton);

            Debug.WriteLine("B: " + skeleton.BackAngle + " -> " + skeleton.BackAngleGroup);
            Debug.WriteLine("S: " + skeleton.ShoulderAngle + " -> " + skeleton.ShoulderAngleGroup);
            Debug.WriteLine("E: " + skeleton.ElbowAngle + " -> " + skeleton.ElbowAngleGroup);
            Debug.WriteLine("A: " + skeleton.AnkleAngle + " -> " + skeleton.AnkleAngleGroup);

            Debug.WriteLine("Upper: " + analyzer.UpperPoseRisk);
            Debug.WriteLine("Lower: " + analyzer.LowerPoseRisk);
        }
    }
}