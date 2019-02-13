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
	public partial class JointSetting : ContentPage
	{
        InfoManager manager;

        private string imagePath = "";

        //Parent로부터 데이터 공유 가능한 카드(Context)를 넘겨 받는 식으로 고칠 것
		public JointSetting ()
		{
			InitializeComponent ();
            manager = InfoManager.GetInfoManager();
            //사진 비율에 따라 LayoutBound 수정
		}

        public void JointSetting_Focused(object sender, FocusEventArgs e)
        {
            if(!imagePath.Equals(manager.PicPath))
            {
                Debug.WriteLine("Recleared");
                imagePath = manager.PicPath;
                WorkImage.Source = ImageSource.FromFile(manager.PicPath);
            }
        }
    }
}