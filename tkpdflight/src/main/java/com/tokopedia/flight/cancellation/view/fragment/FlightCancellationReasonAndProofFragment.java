package com.tokopedia.flight.cancellation.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationReasonAndProofPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationCameraPassData;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.util.FlightAnalytics;

import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FlightCancellationReasonAndProofFragment extends BaseDaggerFragment implements FlightCancellationReasonAndProofContract.View, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener {
    private static final String EXTRA_CANCELLATION_VIEW_MODEL = "EXTRA_CANCELLATION_VIEW_MODEL";
    private static final int REQUEST_CODE_GALLERY = 1001;
    private static int REQUEST_CODE_CAMERA = 1002;
    private ProgressBar progressBar;

    private LinearLayout container;
    private AppCompatEditText etReason;
    private RecyclerView rvAttachments;
    private AppCompatButton btnNext;

    private FlightCancellationAttachmentAdapter adapter;
    private FlightCancellationWrapperViewModel flightCancellationViewModel;
    private OnFragmentInteractionListener interactionListener;
    private String fileFromCameraLocTemp;

    @Inject
    FlightCancellationReasonAndProofPresenter presenter;

    public FlightCancellationReasonAndProofFragment() {

    }

    public static Fragment newInstance(FlightCancellationWrapperViewModel flightCancellationViewModel) {
        FlightCancellationReasonAndProofFragment fragment = new FlightCancellationReasonAndProofFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CANCELLATION_VIEW_MODEL, flightCancellationViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flightCancellationViewModel = getArguments().getParcelable(EXTRA_CANCELLATION_VIEW_MODEL);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        if (savedInstanceState == null) {
            presenter.initialize();
        }
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_refundable_step_two, container, false);
        buildView(view);
        return view;
    }

    private void buildView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        container = (LinearLayout) view.findViewById(R.id.container);
        etReason = (AppCompatEditText) view.findViewById(R.id.et_reason);
        rvAttachments = (RecyclerView) view.findViewById(R.id.rv_attachments);
        btnNext = (AppCompatButton) view.findViewById(R.id.btn_next);
        FlightCancellationAttachmentTypeFactory adapterTypeFactory = new FlightCancellationAttachementAdapterTypeFactory(this);
        adapter = new FlightCancellationAttachmentAdapter(adapterTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttachments.setLayoutManager(layoutManager);
        rvAttachments.setHasFixedSize(true);
        rvAttachments.setNestedScrollingEnabled(false);
        rvAttachments.setAdapter(adapter);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onNextButtonClicked();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    protected String getScreenName() {
        return FlightAnalytics.Screen.FLIGHT_CANCELLATION_STEP_TWO;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        interactionListener = null;
    }

    @Override
    public void showUploadAttachmentView() {
        adapter.showAttachmentButton();
    }

    @Override
    public void hideUploadAttachmentView() {
        adapter.hideAttachmentButton();
    }

    @Override
    public void addAttachment(FlightCancellationAttachmentViewModel viewModel) {
        adapter.addElement(viewModel);
    }

    @Override
    public List<FlightCancellationAttachmentViewModel> getAttachments() {
        return adapter.getData();
    }

    @Override
    public void showRequiredMinimalOneAttachmentErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(resId));
    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationViewModel() {
        return flightCancellationViewModel;
    }

    @Override
    public void showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), errorMessage);
    }

    @Override
    public String getReason() {
        return etReason.getText().toString().trim();
    }

    @Override
    public void showFailedToNextStepErrorMessage(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), errorMessage);
    }

    @Override
    public void navigateToNextStep(FlightCancellationViewModel viewModel) {
        Toast.makeText(getActivity(), "Next Step", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadAttachmentButtonClicked() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setMessage("Bagaimana Anda ingin mengatur gambar Anda ?");
        myAlertDialog.setPositiveButton("Galeri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FlightCancellationReasonAndProofFragmentPermissionsDispatcher.actionOpenGalleryWithCheck(FlightCancellationReasonAndProofFragment.this);
            }
        });
        myAlertDialog.setNegativeButton("Kamera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FlightCancellationReasonAndProofFragmentPermissionsDispatcher.actionOpenCameraWithCheck(FlightCancellationReasonAndProofFragment.this);
            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void deleteAttachement(FlightCancellationAttachmentViewModel element) {
        adapter.removeAttachment(element);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (getActivity() != null && getActivity().getApplication() instanceof FlightModuleRouter) {
            REQUEST_CODE_CAMERA = ((FlightModuleRouter) getActivity().getApplication()).getCameraRequestCode();
        }*/

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (getActivity() != null && getActivity().getApplication() instanceof FlightModuleRouter) {
                    String key = ((FlightModuleRouter) getActivity().getApplication()).getGalleryExtraSelectionPathResultKey();
                    if (data.hasExtra(key)) {
                        String filepath = data.getStringExtra(key);
                        presenter.onSuccessGetImage(filepath);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {

            presenter.onSuccessGetImage(fileFromCameraLocTemp);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionOpenCamera() {
        if (getActivity() != null && getActivity().getApplication() instanceof FlightModuleRouter) {
            FlightCancellationCameraPassData passData = ((FlightModuleRouter) getActivity().getApplication()).startCaptureWithCamera(getActivity());
            fileFromCameraLocTemp = passData.getImagePathLoc();
            startActivityForResult(passData.getDestinationIntent(), REQUEST_CODE_CAMERA);
        }
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionOpenGallery() {
        if (getActivity() != null && getActivity().getApplication() instanceof FlightModuleRouter) {
            Intent intent = ((FlightModuleRouter) getActivity().getApplication()).getGalleryIntent(getActivity());
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
