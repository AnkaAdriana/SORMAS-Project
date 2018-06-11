package de.symeda.sormas.app.event.read.sub;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.app.BaseReadActivityFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.event.EventParticipant;
import de.symeda.sormas.app.core.BoolResult;
import de.symeda.sormas.app.core.IActivityCommunicator;
import de.symeda.sormas.app.core.async.IJobDefinition;
import de.symeda.sormas.app.core.async.ITaskExecutor;
import de.symeda.sormas.app.core.async.ITaskResultCallback;
import de.symeda.sormas.app.core.async.ITaskResultHolderIterator;
import de.symeda.sormas.app.core.async.TaskExecutorFor;
import de.symeda.sormas.app.core.async.TaskResultHolder;
import de.symeda.sormas.app.databinding.FragmentEventReadPersonInvolvedInfoLayoutBinding;
import de.symeda.sormas.app.shared.EventFormNavigationCapsule;

/**
 * Created by Orson on 28/12/2017.
 */

public class EventReadPersonsInvolvedInfoFragment extends BaseReadActivityFragment<FragmentEventReadPersonInvolvedInfoLayoutBinding, EventParticipant, EventParticipant> {

    private AsyncTask onResumeTask;
    private String recordUuid;
    private EventStatus pageStatus;
    private EventParticipant record;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //saveFilterStatusState(outState, filterStatus);
        SavePageStatusState(outState, pageStatus);
        SaveRecordUuidState(outState, recordUuid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = (savedInstanceState != null)? savedInstanceState : getArguments();

        recordUuid = getRecordUuidArg(arguments);
        pageStatus = (EventStatus) getPageStatusArg(arguments);
    }

    @Override
    public boolean onBeforeLayoutBinding(Bundle savedInstanceState, TaskResultHolder resultHolder, BoolResult resultStatus, boolean executionComplete) {
        if (!executionComplete) {
            EventParticipant eventParticipant = getActivityRootData();

            if (eventParticipant != null) {
                if (eventParticipant.isUnreadOrChildUnread())
                    DatabaseHelper.getEventParticipantDao().markAsRead(eventParticipant);
            }

            resultHolder.forItem().add(eventParticipant);
        } else {
            ITaskResultHolderIterator itemIterator = resultHolder.forItem().iterator();

            if (itemIterator.hasNext())
                record = itemIterator.next();
        }

        return true;
    }

    @Override
    public void onLayoutBinding(FragmentEventReadPersonInvolvedInfoLayoutBinding contentBinding) {
        contentBinding.setData(record);
    }

    @Override
    public void onAfterLayoutBinding(FragmentEventReadPersonInvolvedInfoLayoutBinding contentBinding) {

    }

    @Override
    protected void updateUI(FragmentEventReadPersonInvolvedInfoLayoutBinding contentBinding, EventParticipant eventParticipant) {

    }

    @Override
    public void onPageResume(FragmentEventReadPersonInvolvedInfoLayoutBinding contentBinding, boolean hasBeforeLayoutBindingAsyncReturn) {
        if (!hasBeforeLayoutBindingAsyncReturn)
            return;

        try {
            ITaskExecutor executor = TaskExecutorFor.job(new IJobDefinition() {
                @Override
                public void preExecute(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    //getActivityCommunicator().showPreloader();
                    //getActivityCommunicator().hideFragmentView();
                }

                @Override
                public void execute(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    EventParticipant eventParticipant = getActivityRootData();

                    if (eventParticipant != null) {
                        if (eventParticipant.isUnreadOrChildUnread())
                            DatabaseHelper.getEventParticipantDao().markAsRead(eventParticipant);
                    }

                    resultHolder.forItem().add(eventParticipant);
                }
            });
            onResumeTask = executor.execute(new ITaskResultCallback() {
                @Override
                public void taskResult(BoolResult resultStatus, TaskResultHolder resultHolder) {
                    //getActivityCommunicator().hidePreloader();
                    //getActivityCommunicator().showFragmentView();

                    if (resultHolder == null){
                        return;
                    }

                    ITaskResultHolderIterator itemIterator = resultHolder.forItem().iterator();

                    if (itemIterator.hasNext())
                        record = itemIterator.next();

                    if (record != null)
                        requestLayoutRebind();
                    else {
                        getActivity().finish();
                    }
                }
            });
        } catch (Exception ex) {
            //getActivityCommunicator().hidePreloader();
            //getActivityCommunicator().showFragmentView();
        }
    }

    @Override
    protected String getSubHeadingTitle() {
        Resources r = getResources();
        return r.getString(R.string.caption_person_involved);
    }

    @Override
    public EventParticipant getPrimaryData() {
        return record;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public int getReadLayout() {
        return R.layout.fragment_event_read_person_involved_info_layout;
    }

    @Override
    public boolean includeFabNonOverlapPadding() {
        return false;
    }

    public static EventReadPersonsInvolvedInfoFragment newInstance(IActivityCommunicator activityCommunicator, EventFormNavigationCapsule capsule, EventParticipant activityRootData) {
        return newInstance(activityCommunicator, EventReadPersonsInvolvedInfoFragment.class, capsule, activityRootData);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (onResumeTask != null && !onResumeTask.isCancelled())
            onResumeTask.cancel(true);
    }
}
