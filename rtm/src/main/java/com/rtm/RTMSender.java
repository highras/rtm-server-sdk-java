package com.rtm;

import com.fpnn.ErrorRecorder;
import com.fpnn.FPClient;
import com.fpnn.FPData;
import com.fpnn.FPManager;
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.rtm.msgpack.PayloadPacker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RTMSender {

    class ServiceLocker {
        public int status = 0;
    }

    private boolean _destroyed;
    private Thread _serviceThread = null;

    private Object self_locker = new Object();
    private ServiceLocker service_locker = new ServiceLocker();

    private void startServiceThread() {
        synchronized (self_locker) {
            if (this._destroyed) {
                return;
            }
        }

        synchronized (service_locker) {
            if (service_locker.status != 0) {
                return;
            }
            service_locker.status = 1;

            try {
                final RTMSender self = this;
                this._serviceThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        self.serviceThread();
                    }
                });

                try {
                    this._serviceThread.setName("RTM-SENDER");
                } catch (Exception e) {}

                this._serviceThread.start();
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }
        }
    }

    private void serviceThread() {
        try {
            while (true) {
                List<FPManager.IService> list;
                synchronized (service_locker) {
                    if (service_locker.status == 0) {
                        return;
                    }

                    list = this._serviceCache;
                    this._serviceCache = new ArrayList<FPManager.IService>();
                }
                this.callService(list);
                synchronized (service_locker) {
                    service_locker.wait();
                }
            }
        } catch (Exception ex) {
            ErrorRecorder.getInstance().recordError(ex);
        } finally {
            this.stopServiceThread();
        }
    }

    private void callService(List<FPManager.IService> list) {
        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            FPManager.IService is = list.get(i);
            if (is != null) {
                try {
                    is.service();
                } catch (Exception ex) {
                    ErrorRecorder.getInstance().recordError(ex);
                }
            }
        }
    }

    private void stopServiceThread() {
        synchronized (service_locker) {
            if (service_locker.status == 1) {
                try {
                    service_locker.notify();
                } catch (Exception ex) {
                    ErrorRecorder.getInstance().recordError(ex);
                }
                service_locker.status = 0;
            }
        }
    }

    private List<FPManager.IService> _serviceCache = new ArrayList<FPManager.IService>();

    public void addQuest(FPClient client, FPData data, Map<String, Object> payload, FPCallback.ICallback cb, int timeout) {
        if (data == null) {
            if (cb != null) {
                cb.callback(new CallbackData(new Exception("data is null!")));
            }
            return;
        }

        this.addService(new FPManager.IService() {
            @Override
            public void service() {
                if (client != null) {
                    byte[] bytes = new byte[0];
                    PayloadPacker packer = new PayloadPacker();

                    try {
                        packer.pack(payload);
                        bytes = packer.toByteArray();
                    } catch (Exception ex) {
                        ErrorRecorder.getInstance().recordError(ex);
                        if (cb != null) {
                            cb.callback(new CallbackData(ex));
                        }
                        return;
                    }

                    data.setPayload(bytes);
                    client.sendQuest(data, cb, timeout);
                }
            }
        });
    }

    private void addService(FPManager.IService service) {
        synchronized (self_locker) {
            if (this._destroyed) {
                return;
            }
        }

        if (service == null) {
            return;
        }
        this.startServiceThread();

        synchronized (service_locker) {
            if (this._serviceCache.size() < 10000) {
                this._serviceCache.add(service);
            }

            if (this._serviceCache.size() == 9998) {
                ErrorRecorder.getInstance().recordError(new Exception("Quest Calls Limit!"));
            }

            try {
                service_locker.notify();
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }
        }
    }

    public void destroy() {
        synchronized (self_locker) {
            if (this._destroyed) {
                return;
            }
            this._destroyed = true;
        }
        this.stopServiceThread();
    }
}
