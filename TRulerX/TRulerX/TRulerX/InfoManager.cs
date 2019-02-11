﻿using System;
using System.Collections.Generic;
using System.Text;

namespace TRulerX
{
    public class InfoManager
    {
        private static InfoManager manager;
        
        public static InfoManager GetInfoManager()
        {
            if(manager == null)
            {
                manager = new InfoManager();
            }
            return manager;
        }

        private readonly string[] stringVals = new string[10];
        private readonly int[] intVals = new int[10];

        #region Basic Information
        public string Worker { get { return stringVals[0]; } set { stringVals[0] = value; } }
        public string Location { get { return stringVals[1]; } set { stringVals[1] = value; } }
        public string Crop { get { return stringVals[2]; } set { stringVals[2] = value; } }
        public string Work { get { return stringVals[3]; } set { stringVals[3] = value; } }
        public string WorkDetatiled { get { return stringVals[4]; } set { stringVals[4] = value; } }
        public string Weight { get { return stringVals[5]; } set { stringVals[5] = value; } }
        public string Grab { get { return stringVals[6]; } set { stringVals[6] = value; } }
        public int Time { get { return intVals[0]; } set { intVals[0] = value; } }
        public string Analyzer { get { return stringVals[7]; } set { stringVals[7] = value; } }
        public string Bend { get { return stringVals[8]; } set { stringVals[8] = value; } }

        public bool IsPhase1Finished
        {
            get
            {
                if(Time > 0) { return true; }
                else { return false; }
            }
        }
        #endregion

        private InfoManager()
        {
            for (int i = 0; i < 10; i++)
            {
                stringVals[i] = "0";
                intVals[i] = 0;
            }
        }

        public void ShowTest()
        {
            foreach(string str in stringVals)
            {
                Console.WriteLine(str);
            }
        }
    }
}
