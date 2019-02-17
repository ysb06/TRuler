using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

using FormsVideoLibrary;

using TRulerX.Ergonomics;

namespace TRulerX.Phase
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class VideoPage : ContentPage
	{
        public VideoPage (bool[] playingTarget)
		{
			InitializeComponent();

            VisualStateManager.GoToState(Player1Controller, "Stop");
            VisualStateManager.GoToState(Player2Controller, "Stop");

            if (playingTarget == null)
            {
                VideoPlayer1.Source = VideoSource.FromResource("newsroom.mp4");
                VideoPlayer2.Source = VideoSource.FromResource("newsroom.mp4");
            }
            else
            {
                for(int i = 0; i < playingTarget.Length; i ++)
                {
                    Debug.WriteLine("Part " + i + ": " + playingTarget[i]);
                }
                BodyPart part = BodyPart.None;
                if (playingTarget.Length <= 6)
                {
                    if (playingTarget[0])
                    {
                        VisualStateManager.GoToState(NeckButton, "Target");
                        part = BodyPart.Neck;
                    }

                    if (playingTarget[1])
                    {
                        VisualStateManager.GoToState(ShoulderButton, "Target");
                        part = BodyPart.Shoulder;
                    }

                    if (playingTarget[2])
                    {
                        VisualStateManager.GoToState(HandButton, "Target");
                        part = BodyPart.Elbow;
                    }

                    if (playingTarget[3])
                    {
                        VisualStateManager.GoToState(BackButton, "Target");
                        part = BodyPart.Back;
                    }

                    if (playingTarget[4])
                    {
                        VisualStateManager.GoToState(LegButton, "Target");
                        part = BodyPart.Knee;
                    }

                    if (playingTarget[5])
                    {
                        VisualStateManager.GoToState(LegButton, "Target");
                        part = BodyPart.Ankle;
                    }
                }

                if (playingTarget.Length > 0)
                {
                    SetVideoSource(part);
                }
            }
        }

        private void SetVideoSource(BodyPart part)
        {
            switch (part)
            {
                case BodyPart.Neck:
                    VideoPlayer1.Source = VideoSource.FromResource("neck01.mp4");
                    VideoPlayer2.Source = VideoSource.FromResource("neck02.mp4");
                    break;
                case BodyPart.Shoulder:
                    VideoPlayer1.Source = VideoSource.FromResource("shoulder1.mp4");
                    VideoPlayer2.Source = VideoSource.FromResource("shoulder2.mp4");
                    break;
                case BodyPart.Elbow:
                    VideoPlayer1.Source = VideoSource.FromResource("hand1.mp4");
                    VideoPlayer2.Source = VideoSource.FromResource("hand2.mp4");
                    break;
                case BodyPart.Back:
                    VideoPlayer1.Source = VideoSource.FromResource("newsroom.mp4");
                    VideoPlayer2.Source = VideoSource.FromResource("newsroom.mp4");
                    break;
                case BodyPart.Knee:
                case BodyPart.Ankle:
                    VideoPlayer1.Source = VideoSource.FromResource("leg01.mp4");
                    VideoPlayer2.Source = VideoSource.FromResource("leg01.mp4");
                    break;
            }
        }

        protected override void OnAppearing()
        {
            VisualStateManager.GoToState(Player1Controller, "Play");
            VisualStateManager.GoToState(Player2Controller, "Stop");
            VideoPlayer1.Play();
        }

        private void PlayControlButton_Clicked(object sender, EventArgs e)
        {
            if(sender == Player1Controller)
            {
                if(VideoPlayer1.Status == VideoStatus.Playing)
                {
                    VideoPlayer1.Pause();
                    VisualStateManager.GoToState(Player1Controller, "Stop");
                }
                else if(VideoPlayer1.Status == VideoStatus.Paused)
                {
                    VideoPlayer1.Play();
                    VisualStateManager.GoToState(Player1Controller, "Play");
                }
            }

            if (sender == Player2Controller)
            {
                if (VideoPlayer2.Status == VideoStatus.Playing)
                {
                    VideoPlayer2.Pause();
                    VisualStateManager.GoToState(Player2Controller, "Stop");
                }
                else if (VideoPlayer2.Status == VideoStatus.Paused)
                {
                    VideoPlayer2.Play();
                    VisualStateManager.GoToState(Player2Controller, "Play");
                }
            }
        }

        private void BodypartButton_Clicked(object sender, EventArgs e)
        {
            if(sender == NeckButton)
            {
                SetVideoSource(BodyPart.Neck);
            }

            if (sender == ShoulderButton)
            {
                SetVideoSource(BodyPart.Shoulder);
            }

            if (sender == HandButton)
            {
                SetVideoSource(BodyPart.Elbow);
            }

            if (sender == BackButton)
            {
                SetVideoSource(BodyPart.Back);
            }

            if (sender == LegButton)
            {
                SetVideoSource(BodyPart.Knee);
            }
        }
    }
}