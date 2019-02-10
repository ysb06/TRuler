using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AnalysisPage : TabbedPage
    {
        public AnalysisPage ()
        {
            InitializeComponent();
            NavigationPage.SetHasNavigationBar(this, false);
            Children[0] = new BasicInfo();
            Children[0].Title = "정보";
        }
    }
}