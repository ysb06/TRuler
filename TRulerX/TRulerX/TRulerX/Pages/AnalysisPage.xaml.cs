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
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AnalysisPage : TabbedPage
    {
        private InfoManager manager;
        private Page[] initPages = new Page[5];

        public AnalysisPage ()
        {
            InitializeComponent();
            NavigationPage.SetHasNavigationBar(this, false);

            manager = InfoManager.GetInfoManager();

            //todo: BasicInfo를 xaml로 넣지 말고 코드로 삽입할 것
            //todo: 카메라 부분 완성할 것

            for(int i = 0; i < initPages.Length; i++)
            {
                initPages[i] = Children[i];
            }
        }

        private void TabbedPage_CurrentPageChanged(object sender, EventArgs e)
        {
            if(CurrentPage == initPages[1] && manager.IsPhase1Finished)
            {
                Children[1] = new PictureSelection();
            }
        }
    }
}