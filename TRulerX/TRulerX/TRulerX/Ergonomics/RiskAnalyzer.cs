﻿using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;

namespace TRulerX.Ergonomics
{
    public enum PostureType { Upper, Lower }
    public enum UpperPosture { B0_S0_E45, B0_S0_E90, B0_S45_E0, B0_S45_E45, B0_S45_E90, B0_S90_E45, B0_S90_E90, B0_S120_E0, B45_S45_E0, B45_S45_E45, B45_S90_E0, B45_S90_E45, B90_S90_E0, B90_S90_E45 }
    public enum LowerPosture { STD, KF150, KF120, KF90, KF60, KF30, KF30C, KNL1, KNL2, SF_CRS, SC0, SC20, SC40 }

    public class RiskAnalyzer
    {
        public static string[] NAME_UPPER_POSTURE
            = { "B0-S0-E45", "B0-S0-E90", "B0-S45-E0", "B0-S45-E45", "B0-S45-E90", "B0-S90-E45", "B0-S90-E90", "B0-S120-E0", "B45-S45-E0", "B45-S45-E45", "B45-S90-E0", "B45-S90-E45", "B90-S90-E0", "B90-S90-E45" };
	    public static string[] NAME_LOWER_POSTURE	
            = { "STD", "KF150", "KF120", "KF90", "KF60", "KF30", "KF30C", "KNL_1", "KNL_2", "SF_CRS", "SC0", "SC20", "SC40"};

        public UpperPosture Upper_Posture { get; private set; }
        private int B = 0;
        private int S = 0;
        private int E = 0;
        public LowerPosture Lower_Posture { get; private set; }
        private int A = 0;
        
        public int UpperPoseRiskLevel { get; private set; }
        public int LowerPoseRiskLevel { get; private set; }

        public RiskAnalyzer(Skeleton skeleton)
        {
            int backAngle = skeleton.BackAngleGroup;
            int shoulderAngle = skeleton.ShoulderAngleGroup;
            int elbowAngle = skeleton.ElbowAngleGroup;

            B = backAngle;
            S = shoulderAngle;
            E = elbowAngle;

            A = skeleton.AnkleAngleGroup;

            CalcUpperRiskLevel();
            CalcLowerRiskLevel();

            //상지 120은 90으로 처리

        }

        public string GetUpperPosturePath()
        {
            if (B == 0 && S == 0 && E == 45) { return "upper101.png"; }
            else if (B == 0 && S == 0 && E == 90) { return "upper102.png"; }
            else if (B == 0 && S == 45 && E == 0) { return "upper103.png"; }
            else if (B == 0 && S == 45 && E == 45) { return "upper104.png"; }
            else if (B == 0 && S == 45 && E == 90) { return "upper105.png"; }
            else if (B == 0 && S == 90 && E == 45) { return "upper106.png"; }
            else if (B == 0 && S == 90 && E == 90) { return "upper107.png"; }
            else if (B == 0 && S == 125 && E == 0) { return "upper108.png"; }
            else if (B == 45 && S == 45 && E == 0) { return "upper109.png"; }
            else if (B == 45 && S == 45 && E == 45) { return "upper110.png"; }
            else if (B == 45 && S == 90 && E == 0) { return "upper111.png"; }
            else if (B == 45 && S == 90 && E == 45) { return "upper112.png"; }
            else if (B == 90 && S == 90 && E == 0) { return "upper113.png"; }
            else if (B == 90 && S == 90 && E == 45) { return "upper114.png"; }
            else
            {
                Debug.WriteLine("No defined upper risk picture");
                return "upper101.png";
            }
        }

        public string GetCompletePosturePath()
        {
            string complPart = "";

            if (B == 0 && S == 0 && E == 45) { complPart = "posture_00_"; }
            else if (B == 0 && S == 0 && E == 90) { complPart = "posture_01_"; }
            else if (B == 0 && S == 45 && E == 0) { complPart = "posture_02_"; }
            else if (B == 0 && S == 45 && E == 45) { complPart = "posture_03_"; }
            else if (B == 0 && S == 45 && E == 90) { complPart = "posture_04_"; }
            else if (B == 0 && S == 90 && E == 45) { complPart = "posture_05_"; }
            else if (B == 0 && S == 90 && E == 90) { complPart = "posture_06_"; }
            else if (B == 0 && S == 125 && E == 0) { complPart = "posture_07_"; }
            else if (B == 45 && S == 45 && E == 0) { complPart = "posture_08_"; }
            else if (B == 45 && S == 45 && E == 45) { complPart = "posture_09_"; }
            else if (B == 45 && S == 90 && E == 0) { complPart = "posture_10_"; }
            else if (B == 45 && S == 90 && E == 45) { complPart = "posture_11_"; }
            else if (B == 90 && S == 90 && E == 0) { complPart = "posture_12_"; }
            else if (B == 90 && S == 90 && E == 45) { complPart = "posture_13_"; }
            else
            {
                Debug.WriteLine("No defined upper risk picture");
                complPart += "posture_00_";
            }

            if (Lower_Posture == LowerPosture.STD) { complPart += "00"; }
            else if (Lower_Posture == LowerPosture.KF150) { complPart += "01"; }
            else if (Lower_Posture == LowerPosture.KF120) { complPart += "02"; }
            else if (Lower_Posture == LowerPosture.KF90) { complPart += "03"; }
            else if (Lower_Posture == LowerPosture.KF60) { complPart += "04"; }
            else if (Lower_Posture == LowerPosture.KF30) { complPart += "05"; }
            else if (Lower_Posture == LowerPosture.KF30C) { complPart += "06"; }
            else if (Lower_Posture == LowerPosture.KNL1) { complPart += "07"; }
            else if (Lower_Posture == LowerPosture.KNL2) { complPart += "08"; }
            else if (Lower_Posture == LowerPosture.SF_CRS) { complPart += "09"; }
            else if (Lower_Posture == LowerPosture.SC0) { complPart += "10"; }
            else if (Lower_Posture == LowerPosture.SC20) { complPart += "11"; }
            else if (Lower_Posture == LowerPosture.SC40) { complPart += "12"; }

            return complPart + ".png";
        }


        private void CalcUpperRiskLevel()
        {
            if (B == 0 && S == 0 && E == 45)        { UpperPoseRiskLevel = 1; Upper_Posture = UpperPosture.B0_S0_E45; }
            else if (B == 0 && S == 0 && E == 90)   { UpperPoseRiskLevel = 1; Upper_Posture = UpperPosture.B0_S0_E90; }
            else if (B == 0 && S == 45 && E == 0)   { UpperPoseRiskLevel = 2; Upper_Posture = UpperPosture.B0_S45_E0;  }
            else if (B == 0 && S == 45 && E == 45)  { UpperPoseRiskLevel = 2; Upper_Posture = UpperPosture.B0_S45_E45;  }
            else if (B == 0 && S == 45 && E == 90)  { UpperPoseRiskLevel = 2; Upper_Posture = UpperPosture.B0_S45_E90;  }
            else if (B == 0 && S == 90 && E == 45)  { UpperPoseRiskLevel = 3; Upper_Posture = UpperPosture.B0_S90_E45;  }
            else if (B == 0 && S == 90 && E == 90)  { UpperPoseRiskLevel = 3; Upper_Posture = UpperPosture.B0_S90_E90;  }
            else if (B == 0 && S == 125 && E == 0)  { UpperPoseRiskLevel = 4; Upper_Posture = UpperPosture.B0_S120_E0;  }
            else if (B == 45 && S == 45 && E == 0)  { UpperPoseRiskLevel = 2; Upper_Posture = UpperPosture.B45_S45_E0;  }
            else if (B == 45 && S == 45 && E == 45) { UpperPoseRiskLevel = 3; Upper_Posture = UpperPosture.B45_S45_E45;  }
            else if (B == 45 && S == 90 && E == 0)  { UpperPoseRiskLevel = 3; Upper_Posture = UpperPosture.B45_S90_E0;  }
            else if (B == 45 && S == 90 && E == 45) { UpperPoseRiskLevel = 3; Upper_Posture = UpperPosture.B45_S90_E45;  }
            else if (B == 90 && S == 90 && E == 0)  { UpperPoseRiskLevel = 4; Upper_Posture = UpperPosture.B90_S90_E0;  }
            else if (B == 90 && S == 90 && E == 45) { UpperPoseRiskLevel = 4; Upper_Posture = UpperPosture.B90_S90_E45;  }
            else
            {
                Debug.WriteLine("No defined upper risk");
                UpperPoseRiskLevel = -1;
            }
        }

        private void CalcLowerRiskLevel()
        {
            if (A == 30) { LowerPoseRiskLevel = 3; Lower_Posture = LowerPosture.KF30; }        //스쿼팅 부분 세분화 할 것
            else if (A == 60) { LowerPoseRiskLevel = 4; Lower_Posture = LowerPosture.KF60; }
            else if (A == 90) { LowerPoseRiskLevel = 4; Lower_Posture = LowerPosture.KF90;  }
            else if (A == 125) { LowerPoseRiskLevel = 4; Lower_Posture = LowerPosture.KF120;  }
            else if (A == 150) { LowerPoseRiskLevel = 3; Lower_Posture = LowerPosture.KF150;  }
            else if (A == 180) { LowerPoseRiskLevel = 1; Lower_Posture = LowerPosture.STD; }
            else
            {
                Debug.WriteLine("No defined lower risk");
                LowerPoseRiskLevel = -1;
            }
        }

        public void SwitchToPrev(PostureType type)
        {
            int cursor = 0;
            switch (type)
            {
                case PostureType.Upper:
                    cursor = (int)Upper_Posture - 1;
                    if(cursor < 0)
                    {
                        cursor = 13;
                    }
                    Upper_Posture = (UpperPosture)cursor;
                    break;
                case PostureType.Lower:
                    cursor = (int)Lower_Posture - 1;
                    if (cursor < 0)
                    {
                        cursor = 12;
                    }
                    Lower_Posture = (LowerPosture)cursor;
                    break;
            }
        }

        public void SwitchToNext(PostureType type)
        {
            int cursor = 0;
            switch (type)
            {
                case PostureType.Upper:
                    cursor = (int)Upper_Posture + 1;
                    if (cursor > 13)
                    {
                        cursor = 0;
                    }
                    Upper_Posture = (UpperPosture)cursor;
                    break;
                case PostureType.Lower:
                    cursor = (int)Lower_Posture + 1;
                    if (cursor > 12)
                    {
                        cursor = 0;
                    }
                    Lower_Posture = (LowerPosture)cursor;
                    break;
            }
        }
    }
}
