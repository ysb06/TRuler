using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace TRulerX.Phase
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class TitlePage : ContentPage
	{
		public TitlePage ()
		{
			InitializeComponent ();
            NavigationPage.SetHasNavigationBar(this, false);
        }

        private async void Button_Clicked(object sender, EventArgs e)
        {
            await Navigation.PushAsync(new AnalysisPage(), true);
        }
    }
}