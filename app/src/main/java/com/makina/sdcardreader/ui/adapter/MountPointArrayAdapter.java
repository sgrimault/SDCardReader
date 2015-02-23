package com.makina.sdcardreader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makina.sdcardreader.R;
import com.makina.sdcardreader.model.MountPoint;

/**
 * Default {@code Adapter} of {@link com.makina.sdcardreader.model.MountPoint}.
 *  
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class MountPointArrayAdapter extends ArrayAdapter<MountPoint> {

    private int mTextViewResourceId;
    private final LayoutInflater mInflater;

    public MountPointArrayAdapter(Context context) {
        this(
                context,
                R.layout.list_item_mount_point
        );
    }

    public MountPointArrayAdapter(
            Context context,
            int textViewResourceId) {
        super(
                context,
                textViewResourceId
        );

        mTextViewResourceId = textViewResourceId;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(
                    mTextViewResourceId,
                    parent,
                    false
            );
        }
        else {
            view = convertView;
        }

        final MountPoint mountPoint = getItem(position);

        ((TextView) view.findViewById(android.R.id.text1)).setText(mountPoint.getMountPath());

        view.findViewById(android.R.id.icon).setVisibility(View.VISIBLE);

        switch (mountPoint.getStorageType()) {
            case INTERNAL:
                ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_action_hardware_phone_android);
                break;
            case EXTERNAL:
                ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_action_device_sd_storage);
                break;
            case USB:
                ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(R.drawable.ic_action_device_usb);
                break;
            default:
                view.findViewById(android.R.id.icon).setVisibility(View.INVISIBLE);
                break;
        }

        ((TextView) view.findViewById(android.R.id.text2)).setText(mountPoint.getStorageState());

        return view;
    }
}
