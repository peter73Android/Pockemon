package competer73android.httpsgithub.pockemon

import android.location.Location

/**
 * Created by peter on 2/21/18.
 */
class Pockemon {
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var location:Location?=null
    var isCatch:Boolean?=false
    constructor(image:Int, name:String, des:String, power:Double, lat:Double, log:Double){
        this.name=name
        this.des=des
        this.image=image
        this.power=power
        this.location=Location(name)
        this.location!!.latitude=lat
        this.location!!.longitude=log
        this.isCatch=false
    }
}