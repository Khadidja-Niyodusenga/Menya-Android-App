package com.example.smartphoneguide
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import android.os.Build
import androidx.compose.foundation.shape.CircleShape
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme{
                ChatScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var currentMessage by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Menya Android App",
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF004225)
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(
                        "Andika ubutumwa...",
                        style = TextStyle(fontSize = 20.sp)
                    ) }, textStyle = TextStyle(fontSize = 20.sp),

                    colors = TextFieldDefaults.textFieldColors(
                             // placeholder color
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (currentMessage.isNotBlank()) {

                            messages.add("Wowe: $currentMessage")
                            val response = generateResponse(currentMessage)
                            messages.add("Ubufasha: $response")
                            currentMessage = ""

                            coroutineScope.launch {
                          listState.scrollToItem(messages.lastIndex)
                            }
                        }
                    },

                            colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF004225),
                    contentColor = Color.White
                )
                ) {
                    Text("Ohereza")
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                },
                containerColor = Color(0xFF004225),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text("⬆️")
            }
        }




    ) { innerPadding ->
        LazyColumn(
            state = listState,
            reverseLayout = false,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(messages) { msg ->
                Text(
                    text = msg,
                    fontSize = 23.sp,
                    lineHeight = 45.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
var currentState = ""
var currentSection: String? = null
var currentStep = 1

fun generateResponse(input: String): String {
    val message = input.lowercase().trim()

    // Direct access kuri whatsapp
    if (message.contains("urumuri") || message.contains("screen") || message.contains("display")) {
        currentState = "settings"
        currentStep = 4
        return settingsGuide(currentStep)
    }
    if (message.contains("wifi")) {
        currentState = "settings"
        currentStep = 2
        return settingsGuide(currentStep)
    }
    if (message.contains("bluetooth") || message.contains("bulutufu")) {
        currentState = "settings"
        currentStep = 3
        return settingsGuide(currentStep)
    }
    if (message.contains("amajwi") || message.contains("sound")) {
        currentState = "settings"
        currentStep = 5
        return settingsGuide(currentStep)
    }
    if (message.contains("sekirite") || message.contains("umutekano") || message.contains("security")) {
        currentState = "settings"
        currentStep = 6
        return settingsGuide(currentStep)
    }

    // Direct access kuri whatsapp
    if (message.contains("whatsapp") || message.contains("watsap") ||
        message.contains("watsapu") || message.contains("wasapu") || message.contains("wasap")) {

        // Direct access to typed words
        return when {
            message.contains("gushyiraho") || message.contains("install") -> whatsappOptionA()
            message.contains("kwiyandikisha") || message.contains("register") -> whatsappOptionB()
            message.contains("ubutumwa") -> whatsappOptionC(1)
            message.contains("amafoto") || message.contains("videwo") || message.contains("inyandiko") -> whatsappOptionC(2)
            message.contains("guhamagara") || message.contains("gurupe") || message.contains("sitati") -> whatsappOptionC(3)
            else -> whatsappMenu()
        }
    }

    // direct access to imeri
    if (message.contains("email") || message.contains("gmail") ||
        message.contains("imeyili") || message.contains("imeli") || message.contains("imeri")) {

        return when {
            message.contains("gufungura") || message.contains("kwiyandikisha") || message.contains("create account") -> emailOptionA()
            message.contains("kwinjira") || message.contains("login") -> emailOptionB()
            message.contains("koherereza") || message.contains("email nshya") || message.contains("message") || message.contains("ubutumwa") -> emailOptionC(2)
            else -> emailMenu()
        }
    }

    // whole logic here
    return when {
        // setingi menu
        message.contains("setingi") || message.contains("igenamiterere") ||
                message.contains("setting") || message.contains("settingi") -> {
            currentState = "settings"
            currentStep = 0
            settingsGuide()
        }

        // menu ya whatsapp
        message == "whatsapp" -> {
            currentState = "whatsapp"
            currentStep = 0
            whatsappMenu()
        }

        // ✅ Email main menu (if user just says Email)
        message == "email" -> {
            currentState = "email"
            currentStep = 0
            emailMenu()
        }

        // komeza steps
        message.contains("komeza") -> {
            when (currentState) {
                "whatsapp" -> {
                    if (currentStep < 3) {
                        currentStep++
                        whatsappOptionC(currentStep)
                    } else "Ubwo nibwo buryo bw'ingenzi ukeneye kumenya kuri WhatsApp!"
                }
                "email" -> {
                    if (currentStep < 3) {
                        currentStep++
                        emailOptionC(currentStep)
                    } else "Ubwo nibwo buryo bw'ingenzi ukeneye kumenya kuri Email!"
                }
                "settings" -> {
                    if (currentStep < 8) {
                        currentStep++
                        settingsGuide(currentStep)
                    } else "Ubwo nibwo buryo bw'ingenzi ukeneye kumenya kuri Settings!"
                }
                else -> "Banza uhitemo serivisi ushaka gufashwamo (urugero: WhatsApp, Email, Settings...)"
            }
        }

        else -> "Sinkumva neza. Ushobora kubaza ibijyanye na WhatsApp, Email, cyangwa Settings."
    }
}
 fun whatsappMenu(): String {
        return "Ukeneye ubufasha kuri watsapu(WhatsApp). Hitamo: inyuguti y'ubufasha ukeneye muri izi zikurikira\n" +
                "A: Gushyiraho watsapu(WhatsApp)\n" +
                "B: Kwiyandikisha kuri WhatsApp\n" +
                "C: Uko WhatsApp ikoreshwa"
    }

    fun whatsappOptionA(): String {
        return "Kugira ngo ushyire watsapu(WhatsApp) muri telefoni bwa mbere:\n" +
                "1.Jyamurutonde rwa porogaramu zawe \n" +
                "2.Fungura iyanditseho\"Play Store\"  .\n" +
                "3.kanda hasi ahanditse ijambo \"search\" urabona aho kwandika hejuru wandikemo ijambo \"WhatsApp\".\n" +
                "4.Kanda  ahanditse \"Install\".\n" +
                "5.Tegereza ko irangiza insitarasiyo cyangwa kujya muri telefoni yawe, hanyuma kanda \"Open\".\n\n"+

                "💡 Ukeneye internet (data) kugirango ubikore neza.\n\n"+
                "Ushaka gukomeza kwiyandikisha? Andika B"


    }

    fun whatsappOptionB(): String {
        return "Kwiyandikisha kuri watsapu(WhatsApp):\n" +
                "1.Emera amategeko ya WhatsApp (kanda ahanditse\"Agree and Continue\").\n" +
                "2.Hitamo igihugu urugero:(Rwanda +250),Andika numero yawe ya telefoni ushaka gukoresha kuri WhatsApp (nka: 78xxxxxxx).\n" +
                "3.Emeza ko ari yo (kanda OK).\n" +
                "4.Andika code yo kwemeza (izakwohererezwa mu butumwa bugufi SMS).\n" + "Iyo code uyandika aho igusaba kuyishyira.\n" +
                "5.Shyiramo Izina ryawe n’Ifoto,Uzasabwa kwandika izina ushaka ko abandi babona kuri WhatsApp" + "\n" +
                "6.Ushobora no guhitamo ifoto ukoresheje **camera** cyangwa **gallery**, Kanda \"Next\"\n\n"+
                "Ushaka kumenya uko ikoreshwa? (kohereza ubutumwa, guhamagara...) cyangwa ibindi? Andika C"
    }
fun whatsappOptionC(step: Int = 1): String {
    return when (step) {
        1 -> "Uko WhatsApp ikoreshwa :\n" +
                "1. Kohereza ubutumwa (Messages):\n" +
                "- Fungura watsapu(WhatsApp)\n" +
                "- Kanda ku + hasi iburyo urebe abantu ufiteho numero\n" +
                "- Hitamo izina ry'umuntu ushaka kwandikira\n" +
                "- Andika ubutumwa hanyuma ukande ku kimenyetso cy’icyatsi cyangwa umweru kugirango bwoherezwe\n\n" +
                "Andika 'komeza' kugirango ubone uko wakohereza Amafoto,inyandiko n'ibindi"

        2 -> "Uko WhatsApp ikoreshwa:\n" +
                "2. Kohereza amafoto, video cyangwa inyandiko:\n" +
                "- Kanda ku kimenyetso cya 'Clip' cyangwa 'Attach'📎\n" +
                "- Hitamo Camera(igihe ushaka gufata ifoto yako kanya), Gallery(igihe ariho ifoto yawe ibitse), cyangwa Document(igihe ushaka kohereza inyandiko)\n" +
                "- Hitamo ifoto cyangwa inyandiko, hanyuma kanda ahanditse 'Send'\n\n" +
                "3. Kohereza ubutumwa bw’ijwi (Voice Note):\n" +
                "- Fungura ikiganiro\n" +
                "- Kanda kandi ufate ikimenyetso cya Microphone🎙️\n" +
                "- Vuga ubutumwa bwawe, hanyuma urekure kugirango bwoherezwe\n\n" +
                "Andika 'komeza' kugirango ukomeze ku gice cya nyuma cyuburyo wakoreshamo watsapu"

        3 -> "Uko WhatsApp ikoreshwa:\n" +
                "4. Guhamagara abantu:\n" +
                "- Kanda Telephone 📞 cyangwa kamera🎥 mu kiganiro cya watsapu\n\n"+
                "5. Gukoresha Status:\n" +
                "- Kanda Status hejuru, uhitemo kamera📷 cyangwa kereyo✏️ kugirango ushyireho inkuru\n\n" +
                "6. Kureba no gusiba ubutumwa:\n" +
                "- Kanda igihe gito ku butumwa ushaka gusiba\n" +
                "- Hitamo 'Delete for me' kugirango ubutumwa ubusibe muruganiriro rwawe  cyangwa 'Delete for everyone' ubusibe kumpande zombi,haba wowe nuwo waruhaye ubutumwa\n\n" +
                "7. Kuganira mu itsinda:\n" +
                "- Kanda ku + hasi iburyo, uhitemo 'New Group'\n" +
                "- Hitamo abantu, shyiramo izina ry’itsinda hanyuma kanda ikimenyetso cy’icyatsi/umweru\n\n" +
                "Ibi nibyo by’ingenzi kuri WhatsApp!"
        else -> "Andika 1 usubire hejuru ahabanza mugutangira kumenya uko whatsapp ikoreshwa"
    }
}

fun emailMenu(): String {
    return "Ukeneye ubufasha kuri Email. Hitamo:inyuguti y'ubufasha ukeneye muri izi zikurikira\n" +
            "A: kugira konti ya imeri(Gmail) bwa mbere\n" +
            "B: Kwinjira muri imeri(Gmail)\n" +
            "C: Kohereza no kwakira imeri(email)"
}
fun emailOptionA(): String {
    return "Uko washyiraho konti ya imeri(Gmail) nshya muri telefoni yawe:\n\n" +
            "1. Fungura **Settings(Igenamiterere)** kuri telefoni yawe.\n" +
            "2. Manuka hasi ubonemo ahanditse **Accounts(Konti)** hanyuma ukandeho.\n" +
            "3. Kanda **Add Account(Ongeraho Konti)**.\n" +
            "4. Hitamo ahandites **Google**.\n" +
            "5. Hindura konti nshya Ukanda kuri **Create account()** niba udafite email.\n" +
            "6. Injiza amazina yawe (First name na Last name) hanyuma kanda **Next**.\n" +
            "7. Hitamo izina rya email (urugero: izinaryawe@gmail.com) hanyuma kanda **Next**.\n" +
            "8. Shyiramo Password nshya kandi wemeze, hanyuma kanda **Next**.\n" +
            "9. Emera amabwiriza ya Google, hanyuma konti ya Gmail iba yashyizweho.\n\n" +
            "Ubu ushobora gukoresha iyi konti kuri Gmail, YouTube, na Play Store.\n\n"+
            "urashaka kumenya uko wakwinjira muri Gmail yawe cyangwa muri konti yawe nshya? andika B"

}
fun emailOptionB(): String {
    return "Uko winjira muri Gmail cyangwa wongeramo konti nshya:\n\n" +
            "1. Fungura porogaramu ya **Gmail** muri telefoni yawe.\n" +
            "2. Iyo ubona urupapuro rukubaza konti, kanda ahanditse **Add account(Ongeraho Konti)**.\n" +
            "3. Hitamo **Google**.\n" +
            "4. Injiza Email yawe na Password(ijambo banga) yawe.\n" +
            "5. Niba ari ubwa mbere, kanda ahanditse **I Agree(Nemera)** kugira ngo winjire.\n\n" +
            "Ubu ushobora kubona ubutumwa wohererejwe muri Gmail (Inbox)."
}

fun emailOptionC(step: Int = 1): String {
    return when (step) {
        1 -> "Uko wakira Email muri Gmail:\n\n" +
                "1. Fungura porogaramu yawe unyuze mu rutonde rwa porogaramu zose muri telefoni yawe, ukande kuri **Gmail**.\n" +
                "2. Urahita ubona aho handitse **Inbox** harimo Emails zakohererejwe.\n" +
                "3. Kanda kuri Email ushaka gusoma kugira ngo uyifungure.\n\n" +
                "Andika 'komeza' kugira ngo ubone uko wohereza Email nshya."

        2 -> "Uko wohereza Email nshya:\n\n" +
                "1. Fungura porogaramu ya **Gmail**.\n" +
                "2. Kanda ku kimenyetso cya **agakereyo✏️** cyangwa ahanditse **Compose** (hasi iburyo).\n" +
                "3. Mu gasanduku ka **To**, andika Email y’uwo ushaka koherereza urugero: umuntu@gmail.com\n" +
                "4. Ahanditse **Subject**, andika umutwe cyangwa impamvu y’iyo Email.\n\n" +
                "Andika 'komeza' kugira ngo ubone uko urangiza ubutumwa bwawe no kubwohereza."

        3 -> "Uko urangiza kohereza Email:\n\n" +
                "5. Mu gasanduku kanini hasi, andika ubutumwa bwawe.\n" +
                "6. Niba ushaka kongeraho ifoto cyangwa inyandiko, kanda kuri **Clip/Attach**📎.\n" +
                "7. Ohereza Ukanda **Send** kugira ngo Email yawe igende.\n\n" +
                "Ubu ushobora kohereza no kwakira mesagje za Email neza muri telefoni yawe!"

        else -> "Andika 1 kugira ngo utangire kwiga uko Email ikoreshwa kuva ku ntangiriro."
    }
}
    fun handleOptionA(): String {
        return when (currentState) {
            "whatsapp" -> whatsappOptionA()
            "email" -> emailOptionA()
            else -> "sinkumva neza. Andika ijambo risobanutse cyangwa uhitamo imwe munyuguti zagaragajwe hejuru"
        }
    }

    fun handleOptionB(): String {
        return when (currentState) {
            "whatsapp" -> whatsappOptionB()
            "email" -> emailOptionB()
            else -> "sinkumva neza. Andika ijambo risobanutse cyangwa uhitamo imwe munyuguti zagaragajwe hejuru"
        }
    }

    fun handleOptionC(): String {
        return when (currentState) {
            "whatsapp" -> whatsappOptionC()
                "email" -> emailOptionC()
            else -> "sinkumva neza. Andika ijambo risobanutse cyangwa uhitamo imwe munyuguti zagaragajwe hejuru"
        }
    }
fun settingsGuide(step: Int = 1): String {
    return when (step) {
        // Intangiriro
        1 -> "Uko wakoresha Igenamiterere (Settings) muri telefoni:\n" +
                "1. Shaka ikimenyetso cya **Settings** (⚙️) muri telefoni.\n" +
                "2. Kanda kugira ngo ufungure.\n" +
                "3. Urabona ibyiciro bitandukanye nka Wi-Fi, Bluetooth, Display, Sound, na Security.\n\n" +
                "Andika 'komeza' kugira ngo utangire kureba uko wakoresha Wi-Fi, cyangwa aho ushaka guhera muri ibi byiciro bitandukanye biri muri settings nka Display,Security,...."
        // Interinete
        2 -> "🔹 Uko uhuza Wi-Fi:\n" +
                "1. Mu Igenamiterere (Settings), kanda kuri **Wi-Fi**.\n" +
                "2. Hanyuma uzabona urutonde rw’imiyoboro (networks).\n" +
                "3. Hitamo izina ry’urwo ushaka (urugero: Home_WiFi).\n" +
                "4. Andika ijambo ry’ibanga (password).\n" +
                "5. Kanda **Connect**.\n\n" +
                "Niba ijambo ry’ibanga ari ryo, uzabona ko Wi-Fi yifatanyije (Connected).\n\n" +
                "Andika 'komeza' kugira ngo ukomeze ku bijyanye na Bluetooth."
        // Bulutufu
        3 -> "🔹 Uko ukoresha Bluetooth:\n" +
                "1. Mu Igenamiterere (Settings), kanda kuri **Bluetooth**.\n" +
                "2. Hanyuma wemeze ko Bluetooth iri **ON** (ihishwe cyangwa yanditse 'On').\n" +
                "3. Telefoni izatangira gushaka ibikoresho biri hafi.\n" +
                "4. Hitamo izina ry’igikoresho (urugero: JBL Speaker cyangwa Earbuds).\n" +
                "5. Kanda **Pair** (Guhuza).\n" +
                "6. Niba bisaba, andika kode (urugero: 0000 cyangwa 1234).\n\n" +
                "Iyo byahuye neza, uzabona izina ry’igikoresho rigaragara kuri 'Connected'.\n\n" +
                "Andika 'komeza' kugira ngo ukomeze kuri Display."
        // Urunuri
        4 -> "🔹 Uko uhindura Display (urumuri):\n" +
                "1. Mu Igenamiterere, kanda kuri **Display** cyangwa **Screen**.\n" +
                "2. Hitamo **Brightness** kugirango wongere cyangwa ugabanye urumuri.\n" +
                "3. Ushobora no guhitamo **Dark Mode**(uburyo bw'ijimye) cyangwa **Light Mode**(uburyo bugaragara).\n" +
                "4. Hari aho ushobora guhindura ingano y’inyuguti (Font Size).\n\n" +
                "Andika 'komeza' kugira ngo ukomeze kuri Sound(Amajwi)."
        // Amajwi
        5 -> "🔹 Uko uhindura amajwi (Sound):\n" +
                "1. Mu Igenamiterere, kanda kuri **Sound**.\n" +
                "2. Uhindure urusaku rw’amatelefone (Ringtone), notification, n’indirimbo ihamagara.\n" +
                "3. Ushobora guhitamo indirimbo nshya cyangwa soneri(Select Ringtone).\n" +
                "4. Kora igenzura: hamagara telefoni yawe urebe ko amajwi akora.\n\n" +
                "Andika 'komeza' kugira ngo ukomeze kuri Sekirite(Umutekano)."
        // Umutekano
        6 -> "🔹 Uko ushaka umutekano (Security):\n" +
                "1. Mu Igenamiterere, kanda kuri **Security**.\n" +
                "2. Hitamo **Screen Lock** kugirango ubashe gufunga Mugaragaza(screen).\n" +
                "3. Ushobora guhitamo Ishusho(Pattern), Umubare w’ibanga(PIN) cyangwa Ijambobanga(Password).\n" +
                "4. Niba telefoni yawe ifite Igikumwe (Fingerprint) cyangwa Gufungura ukoresheje Isura (Face Unlock), kanda kuri byo ukurikize amabwiriza..\n" +
                "5. Gerageza gufunga no kongera gufungura telefoni urebe umutekano washyize muri telefoni yawe wagiyemo.\n\n" +
                "Andika 'komeza' kugira ngo ukomeze ku gice cya nyuma."
        // igice cyanyuma
        7 -> "🔹 Igenamiterere ryisumbuye (Advanced Settings):\n\n" +

                "**Apps (Porogaramu):**\n" +
                "- Kanda kuri **Apps** cyangwa **Applications**.\n" +
                "- Urabona urutonde rwa porogaramu zose ziri muri telefoni yawe.\n" +
                "- Ushobora gukanda ku izina rya application kugira ngo urebe amahitamo.\n" +
                "- Hari ahanditse **Uninstall** (gusiba burundu) niba porogaramu utayikeneye.\n" +
                "- Hari kandi **Force Stop** (Guhagarika by’igihe gito) iyo porogaramu yagize ikibazo cyangwa iri kurondereza umuriro wa bateri.\n\n" +

                "**Storage (Ububiko):**\n" +
                "- Kanda kuri **Storage**(Ububiko).\n" +
                "- Hano urabona uburyo ububiko bwa telefoni bukoreshwa (amafoto, videwo, porogaramu, inyandiko n’ibindi).\n" +
                "- Ushobora gusiba amafoto, videwo cyangwa porogaramu zidafite akamaro kugira ngo ubone umwanya cyangwa ububiko.\n\n" +

                "**Battery (Bateri):**\n" +
                "- Kanda kuri **Battery**.\n" +
                "- Urahita ubona uko umuriro wa bateri ukoreshwa na porogaramu ikoresha umuriro cyane.\n" +
                "- Ushobora guhitamo **Battery Saver** kugira ngo umuriro wa bateri urambe igihe kinini, cyane cyane iyo umuriro uri hafi gushira.\n\n" +

                "**System Update (Kuvugurura):**\n" +
                "- Kanda kuri **System Update** cyangwa **Software Update**.\n" +
                "- Telefoni izasuzuma niba hari version nshya ya Android iboneka.\n" +
                "- Niba ibonetse, kanda **Download and Install**.\n" +
                "- Kuvugurura Android bituma telefoni ikora neza, ikagira umutekano mwinshi kandi ikabona uburyo bushya.\n\n" +

                "Noneho wamenye uburyo bwo gukoresha neza igenamiterere (Settings) mu buryo bwuzuye!"

        else -> "Andika 1 kugira ngo utangire kwiga ku bijyanye na Settings kuva ku ntangiriro."
    }
}


