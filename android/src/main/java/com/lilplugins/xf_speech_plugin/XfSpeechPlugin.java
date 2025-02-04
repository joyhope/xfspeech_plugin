package com.lilplugins.xf_speech_plugin;

import android.util.Log;
import android.app.Activity;

import androidx.annotation.NonNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;


public class XfSpeechPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware{

  private static String TAG = XfSpeechPlugin.class.getSimpleName();
  private static final String CHANNEL = "lilplugins.com/xf_speech_plugin";
  
  PluginRegistry.Registrar registrar;
  XfSpeechDelegate delegate;

  private MethodChannel channel;
  private Activity mActivity;

  /*  
  XfSpeechPlugin() {
  }

  XfSpeechPlugin(final PluginRegistry.Registrar registrar, final XfSpeechDelegate delegate) {
    this.registrar = registrar;
    this.delegate = delegate;
  }
  */
  
  public static void registerWith(Registrar registrar) {
    final XfSpeechPlugin instance = new XfSpeechPlugin();
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
    final XfSpeechDelegate delegate = new XfSpeechDelegate(registrar.activity(),channel);
    registrar.addRequestPermissionsResultListener(delegate);

    instance.registrar = registrar;
    instance.delegate = delegate;
    channel.setMethodCallHandler(instance);
  }
  
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
      onAttachedToActivity(binding.getActivity());
      binding.addRequestPermissionsResultListener(this.delegate);
  }

  private void onAttachedToActivity(Activity activity) {
      this.mActivity = activity;
      this.delegate = new XfSpeechDelegate(activity, this.channel);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
      onAttachedToActivity(binding);
      binding.addRequestPermissionsResultListener(this.delegate);
  }

  @Override
  public void onDetachedFromActivity() {
      this.mActivity = null;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  static final String METHOD_CALL_INITWITHAPPID = "initWithAppId";
  static final String METHOD_CALL_SETPARAMETER = "setParameter";

  static final String METHOD_CALL_STARTLISTENING = "startListening";
//  static final String METHOD_CALL_WRITEAUDIO = "writeAudio";
  static final String METHOD_CALL_STOPLISTENING = "stopListening";
//  static final String METHOD_CALL_ISLISTENING = "isListening";
  static final String METHOD_CALL_CANCELLISTENING = "cancelListening";


  static final String METHOD_CALL_STARTSPEAKING = "startSpeaking";
  static final String METHOD_CALL_PAUSESPEAKING = "pauseSpeaking";
  static final String METHOD_CALL_RESUMESPEAKING = "resumeSpeaking";
  static final String METHOD_CALL_STOPSPEAKING = "stopSpeaking";
  static final String METHOD_CALL_ISSPEAKING = "isSpeaking";


  static final String METHOD_CALL_DISPOSE = "dispose";


  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (registrar.activity() == null) {
      result.error("no_activity", "image_cropper plugin requires a foreground activity.", null);
      return;
    }

    Log.e(TAG, call.method);
    switch (call.method) {
      case METHOD_CALL_INITWITHAPPID:
        delegate.initWithAppId(call, result);  // recongnnizer & synthesizer
        break;
      case METHOD_CALL_SETPARAMETER:  // recongnnizer & synthesizer
        delegate.setParameter(call, result);
        break;
      case METHOD_CALL_DISPOSE: // recongnnizer & synthesizer
        delegate.dispose(call, result);
        break;


      case METHOD_CALL_STARTLISTENING:
        delegate.startListening(call, result);
        break;
      case METHOD_CALL_STOPLISTENING:
        delegate.stopListening(call, result);
        break;
      case METHOD_CALL_CANCELLISTENING:
        delegate.cancelListening(call, result);
        break;

      case METHOD_CALL_STARTSPEAKING:
        delegate.startSpeaking(call, result);
        break;
      case METHOD_CALL_PAUSESPEAKING:
        delegate.pauseSpeaking(call, result);
        break;
      case METHOD_CALL_RESUMESPEAKING:
        delegate.resumeSpeaking(call, result);
        break;
      case METHOD_CALL_STOPSPEAKING:
        delegate.stopSpeaking(call, result);
        break;

      default:
        throw new IllegalArgumentException("Unknown method " + call.method);
    }
  }

}
