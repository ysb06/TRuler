using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Plugin.Media;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class BasicInfo : ContentPage
    {
        private InfoManager manager;

        public BasicInfo()
        {
            InitializeComponent();
            manager = InfoManager.GetInfoManager();
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


        private void Param1_worker_Unfocused(object sender, FocusEventArgs e) { manager.Worker = Param1_worker.Text; }
        private void Param2_location_Unfocused(object sender, FocusEventArgs e) { manager.Location = Param2_location.Text; }
        private void Param3_crop_Unfocused(object sender, FocusEventArgs e) { manager.Crop = Param3_crop.Text; }
        private void Param4_work_Unfocused(object sender, FocusEventArgs e) { manager.Work = Param4_work.Text; }
        private void Param5_workDetailed_Unfocused(object sender, FocusEventArgs e) { manager.WorkDetatiled = Param5_workDetailed.Text; }
        private void Param6_weight_Unfocused(object sender, FocusEventArgs e) { manager.Weight = Param6_weight.Text; }
        private void Param7_grab_Unfocused(object sender, FocusEventArgs e) { manager.Grab = Param7_grab.Text; }
        private void Param8_time_Unfocused(object sender, FocusEventArgs e) { manager.Time = int.Parse(Param8_time.Text); }
        private void Param9_analyzer_Unfocused(object sender, FocusEventArgs e) { manager.Analyzer = Param9_analyzer.Text; }
        private void Param10_bend_Unfocused(object sender, FocusEventArgs e) { manager.Bend = Param10_bend.SelectedItem.ToString(); }

        private void Cam_Button_Clicked(object sender, EventArgs e)
        {

        }

        private void Image_Button_Clicked(object sender, EventArgs e)
        {

        }
    }
}