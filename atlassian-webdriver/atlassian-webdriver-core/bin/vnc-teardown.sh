function killVnc {
    echo stopping vncserver on :20

    VNC_PID_FILE=`echo $HOME/.vnc/*:20.pid`
    if [ -n "$VNC_PID_FILE" -a -f "$VNC_PID_FILE" ]; then
        vncserver -kill :20 2>&1
        if [ -f  "$VNC_PID_FILE" ]; then
            VNC_PID=`cat $VNC_PID_FILE`
            echo "Killing VNC pid ($VNC_PID) directly..."
            kill -9 $VNC_PID
            vncserver -kill :20 2>&1

            if [ -f  "$VNC_PID_FILE" ]; then
                echo "Failed to kill vnc server"
                exit -1
            fi
        fi
    fi
}

trap killVnc INT TERM EXIT