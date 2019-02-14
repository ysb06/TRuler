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
    public delegate void TRulerEventHandler(PhasePage page, RequestParam param);

    public enum PhasePage { BASIC_INFO, PICTURE_SELECTION, JOINT_SETTING, POSE_CONFIRMATION, RESULT }
    public enum RequestParam { NONE, ACTIVATE_NEXT, CANCEL_ACTIVATION }

    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AnalysisPage : TabbedPage
    {
        private InfoManager manager;

        private BasicInfo P1_BasicInfo;
        private PictureSelection P2_PictureSelection;
        private JointSetting P3_JointSetting;
        private PoseConfirmation P4_PoseConfirmation;
        private Page temp;
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

            P2_PictureSelection.ProgressStateChanged += Page_NextRequested;
            P3_JointSetting.ProgressStateChanged += Page_NextRequested;
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
                case PhasePage.JOINT_SETTING:
                    if(!Phase4Activated)
                    {
                        switch(param)
                        {
                            case RequestParam.ACTIVATE_NEXT:
                                temp = Children[3];
                                Children[3] = P4_PoseConfirmation;
                                Phase4Activated = true;
                                break;
                            case RequestParam.CANCEL_ACTIVATION:
                                Children[3] = temp;
                                Phase4Activated = false;
                                break;
                        }
                    }
                    break;
            }
        }

        private void TabbedPage_CurrentPageChanged(object sender, EventArgs e)
        {
            Debug.WriteLine("Count: " + Children.Count);
            Debug.WriteLine("Current: " + CurrentPage);
        }
       
    }
}