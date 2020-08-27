package vn.amit.sendmail

interface SendMailService {
    fun sendMailMessage(to:String,sub:String,text:String)
    fun sendMailMessage(to: String, sub: String, arguments:Map<String,Any?>, template: String)
    fun sendMailMessage(to: String, sub: String, arguments:Map<String,Any?>, template: MailTemplate)
}