using System;   
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

using TRulerX.Phase.PhaseTools;

namespace TRulerX.Phase
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class BasicInfo : ContentPage
	{
        private AnalysisSheet sheet;

        public BasicInfo()
        {
            InitializeComponent();
            sheet = new AnalysisSheet(true);
        }

		public BasicInfo (AnalysisSheet sheet)
		{
			InitializeComponent ();
            this.sheet = sheet;
		}

        private void Param1_worker_Completed(object sender, EventArgs e) { Param2_location.Focus(); }
        private void Param2_location_Completed(object sender, EventArgs e) { Param3_crop.Focus(); }
        private void Param3_crop_Completed(object sender, EventArgs e) { Param4_work.Focus(); }
        private void Param4_work_Completed(object sender, EventArgs e) { Param5_workDetailed.Focus(); }
        private void Param5_workDetailed_Completed(object sender, EventArgs e) { Param6_weight.Focus(); }
        private void Param6_weight_Completed(object sender, EventArgs e) { Param7_grab.Focus(); }
        private void Param7_grab_Completed(object sender, EventArgs e) { Param8_time.Focus(); }
        private void Param8_time_Completed(object sender, EventArgs e) { Param9_analyzer.Focus(); }
        private void Param9_analyzer_Completed(object sender, EventArgs e) { Param10_bend.Focus(); }


        private void Param1_worker_Unfocused(object sender, FocusEventArgs e) { sheet.Worker = Param1_worker.Text; }
        private void Param2_location_Unfocused(object sender, FocusEventArgs e) { sheet.Location = Param2_location.Text; }
        private void Param3_crop_Unfocused(object sender, FocusEventArgs e) { sheet.Crop = Param3_crop.Text; }
        private void Param4_work_Unfocused(object sender, FocusEventArgs e) { sheet.Work = Param4_work.Text; }
        private void Param5_workDetailed_Unfocused(object sender, FocusEventArgs e) { sheet.WorkDetatiled = Param5_workDetailed.Text; }
        private void Param6_weight_Unfocused(object sender, FocusEventArgs e) { sheet.Weight = Param6_weight.Text; }
        private void Param7_grab_Unfocused(object sender, FocusEventArgs e) { sheet.Grab = Param7_grab.Text; }
        private void Param8_time_Unfocused(object sender, FocusEventArgs e) { sheet.Time = int.Parse(Param8_time.Text); }
        private void Param9_analyzer_Unfocused(object sender, FocusEventArgs e) { sheet.Analyzer = Param9_analyzer.Text; }
        private void Param10_bend_Unfocused(object sender, FocusEventArgs e) { sheet.NeckBend = (string)Param10_bend.SelectedItem; }

        private void Cam_Button_Clicked(object sender, EventArgs e)
        {
            ((AnalysisPage)Parent).SwitchNext();
        }

        private void Image_Button_Clicked(object sender, EventArgs e)
        {

        }
    }
}