package com.chszs;
class GeneralHashFunctionLibrary
{
   public long BKDRHash(String str)
   {
      long seed = 131;
      long hash = 0;

      for(int i = 0; i < str.length(); i++)
      {
         hash = (hash * seed) + str.charAt(i);
      }

      return hash;
   }
}
