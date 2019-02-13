using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Pages
{
    public delegate void NextEventHandler(PhasePage page, RequestParam param);

    public enum PhasePage { BASIC_INFO, PICTURE_SELECTION, JOINT_SETTING, POSE_CONFIRMATION, RESULT }
    public enum RequestParam { NONE, RUN_CAMERA, RUN_GALLERY }

    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AnalysisPage : TabbedPage
    {
        private InfoManager manager;

        private BasicInfo P1_BasicInfo;
        private PictureSelection P2_PictureSelection;
        private JointSetting P3_JointSetting;
        private PoseConfirmation P4_PoseConfirmation;
        private ResultPage P5_Result;

        public bool Phase1Activated { get; private set; }
        public bool Phase2Activated { get; private set; }
        public bool Phase3Activated { get; private set; }
        public bool Phase4Activated { get; private set; }
        public bool Phase5Activated { get; private set; }

        public AnalysisPage ()
        {
            InitializeComponent();

            P1_BasicInfo = new BasicInfo();
            P2_PictureSelection = new PictureSelection();
            P3_JointSetting = new JointSetting();
            P4_PoseConfirmation = new PoseConfirmation();
            P5_Result = new ResultPage();

            Children[0] = P1_BasicInfo;
            Phase1Activated = true;
            Children[1] = P2_PictureSelection;
            Phase2Activated = true;
            Phase3Activated = false;
            Phase4Activated = false;
            Phase5Activated = false;

            NavigationPage.SetHasNavigationBar(this, false);
            manager = InfoManager.GetInfoManager();

            P2_PictureSelection.NextRequested += Page_NextRequested;
        }

        private void Page_NextRequested(PhasePage page, RequestParam param)
        {
            switch(page)
            {
                case PhasePage.PICTURE_SELECTION:
                    if(!Phase3Activated)
                    {
                        Children[2] = P3_JointSetting;
                        Phase3Activated = true;
                    }
                    break;
            }
        }

        private void TabbedPage_CurrentPageChanged(object sender, EventArgs e)
        {
            Debug.WriteLine("Count: " + Children.Count);
            Debug.WriteLine("Current: " + CurrentPage);
            if(Children.Count >= 2 && CurrentPage == Children[2])
            {
                P3_JointSetting.JointSetting_Focused(this, new FocusEventArgs(this, true));
            }
        }
       
    }
}