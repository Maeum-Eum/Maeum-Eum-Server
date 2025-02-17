package com.five.Maeum_Eum.dto.center.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyCenterReq {

     String oneLineIntro;

     public ModifyCenterReq(String oneLineIntro){
         this.oneLineIntro = oneLineIntro;
     }


}
