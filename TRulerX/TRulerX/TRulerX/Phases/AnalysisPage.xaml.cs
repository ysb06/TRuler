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
using System.Collections.Specialized;

namespace TRulerX.Phase
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AnalysisPage : TabbedPage
    {
        private AnalysisSheet sheet;

        private BasicInfo P1_BasicInfo;
        private PictureSelection P2_PictureSelection;
        private JointSetting P3_JointSetting;
        private PoseConfirmation P4_PoseConfirmation;
        private Result P5_Result;

        public AnalysisPage ()
        {
            InitializeComponent();
            NavigationPage.SetHasNavigationBar(this, false);

            sheet = new AnalysisSheet();

            P1_BasicInfo = new BasicInfo(sheet);
            P2_PictureSelection = new PictureSelection(sheet);
            P3_JointSetting = new JointSetting(sheet);
            P4_PoseConfirmation = new PoseConfirmation(sheet);
            P5_Result = new Result(sheet);

            Children[0] = P1_BasicInfo;
            Children[1] = P2_PictureSelection;
        }

        public void SwitchNext()
        {
            for(int i = 0; i < Children.Count; i++)
            {
                if (CurrentPage == Children[i])
                {
                    if (i + 1 >= Children.Count)
                    {
                        CurrentPage = Children[Children.Count - 1];
                        break;
                    }
                    else
                    {
                        CurrentPage = Children[i + 1];
                        break;
                    }
                }
            }
        }

        public void SetNextReady(PhaseType current)
        {
            switch(current)
            {
                case PhaseType.PICTURE_SELECTION:
                    if (Children[2] != P3_JointSetting)
                    {
                        Children[2] = P3_JointSetting;
                    }
                    break;
                case PhaseType.JOINT_SETTING:
                    if (Children[3] != P4_PoseConfirmation)
                    {
                        Children[3] = P4_PoseConfirmation;
                    }
                    break;
                case PhaseType.POSE_CONFIRMATION:
                    if (Children[4] != P5_Result)
                    {
                        Children[4] = P5_Result;
                    }
                    break;
            }
        }

        protected override void OnCurrentPageChanged()
        {
            if (CurrentPage == P3_JointSetting)
            {
                P3_JointSetting.OnActivated();
            }
            else if(CurrentPage == P4_PoseConfirmation)
            {
                P4_PoseConfirmation.OnActivated();
            }
            else if(CurrentPage == P5_Result)
            {
                P5_Result.OnActivated();
            }
        }
    }
}