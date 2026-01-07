package com.example.lifeos.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import com.example.lifeos.LifeOSApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                if (!incomingNumber.isNullOrEmpty()) {
                    handleIncomingCall(context, incomingNumber)
                }
            }
        }
    }

    private fun handleIncomingCall(context: Context, number: String) {
        val pendingResult = goAsync()
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
             try {
                 val app = context.applicationContext as LifeOSApplication
                 val repo = app.container.settingsRepository
                 val settings = repo.getSettingsSnapshot()
                 
                 val focusMode = settings?.currentFocusMode ?: "None"
                 val isFocusActive = focusMode != "None"

                  if (isFocusActive && settings != null) {
                      val count = com.example.lifeos.data.manager.FocusManager.incrementAndGetCount(number)
                      
                      if (settings.allowOverride && count > 3 && settings.isSilentModeEnabledForFocus) {
                          // Emergency Override Triggered
                          repo.updateSettings(settings.copy(isSilentModeEnabledForFocus = false))
                          
                          val audioManager = context.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
                          try {
                              audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_NORMAL
                          } catch (e: Exception) { e.printStackTrace() }
                      }
                  }

                  if (isFocusActive && settings?.isAutoReplyEnabled == true) {
                     val whitelist = settings.sleepWhitelist.split(",").filter { it.isNotBlank() }
                     val normalizedIncoming = number.replace("[^0-9]".toRegex(), "")
                     
                     val isWhitelisted = whitelist.any { w -> 
                         val normalizedW = w.replace("[^0-9]".toRegex(), "")
                         normalizedIncoming.endsWith(normalizedW) || normalizedW.endsWith(normalizedIncoming) 
                     }
                     
                     val isSavedContact = isContactPersonal(context, number)
                     
                     val messageToSend: String? = if (isWhitelisted) {
                         // Whitelisted logic - Use custom reply if set
                         val custom = when(focusMode) {
                              "Sleep" -> settings.customReplySleep
                              "Driving" -> settings.customReplyDriving
                              "Meeting" -> settings.customReplyMeeting
                              else -> null
                         }
                         if (!custom.isNullOrBlank()) custom else "I am $focusMode - powered by LifeOS"
                     } else if (isSavedContact) {
                         // Saved Contact but NOT Whitelisted
                         "I will call you later - powered by LifeOS"
                     } else {
                         // Unknown caller -> No message
                         null
                     }
                     
                     if (messageToSend != null) {
                         try {
                              val smsManager = context.getSystemService(SmsManager::class.java)
                              if (smsManager != null) {
                                  smsManager.sendTextMessage(number, null, messageToSend, null, null)
                              } else {
                                  @Suppress("DEPRECATION")
                                  SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null)
                              }
                              
                              // Log the callback
                              val callbackRepo = app.container.callbackRepository
                              callbackRepo.logCallback(
                                  com.example.lifeos.data.local.entity.CallbackLogEntity(
                                      date = java.time.LocalDateTime.now(),
                                      phoneNumber = number,
                                      messageSent = messageToSend
                                  )
                              )
                              
                         } catch (e: Exception) {
                              e.printStackTrace()
                         }
                     }
                 }
             } catch(e: Exception) {
                 e.printStackTrace()
             } finally {
                 pendingResult.finish()
             }
        }
    }

    private fun isContactPersonal(context: Context, number: String): Boolean {
        // Build a query to check if the number exists in contacts
        // Using PhoneLookup for efficiency
        val uri = android.net.Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, android.net.Uri.encode(number))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        
        try {
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            return cursor?.use { it.count > 0 } ?: false
        } catch (e: SecurityException) {
            return false
        } catch (e: Exception) {
            return false
        }
    }
}
