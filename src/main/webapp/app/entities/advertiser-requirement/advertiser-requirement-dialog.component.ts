import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AdvertiserRequirement } from './advertiser-requirement.model';
import { AdvertiserRequirementPopupService } from './advertiser-requirement-popup.service';
import { AdvertiserRequirementService } from './advertiser-requirement.service';

@Component({
    selector: 'jhi-advertiser-requirement-dialog',
    templateUrl: './advertiser-requirement-dialog.component.html'
})
export class AdvertiserRequirementDialogComponent implements OnInit {

    advertiserRequirement: AdvertiserRequirement;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private advertiserRequirementService: AdvertiserRequirementService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.advertiserRequirement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.advertiserRequirementService.update(this.advertiserRequirement));
        } else {
            this.subscribeToSaveResponse(
                this.advertiserRequirementService.create(this.advertiserRequirement));
        }
    }

    private subscribeToSaveResponse(result: Observable<AdvertiserRequirement>) {
        result.subscribe((res: AdvertiserRequirement) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AdvertiserRequirement) {
        this.eventManager.broadcast({ name: 'advertiserRequirementListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-advertiser-requirement-popup',
    template: ''
})
export class AdvertiserRequirementPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private advertiserRequirementPopupService: AdvertiserRequirementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.advertiserRequirementPopupService
                    .open(AdvertiserRequirementDialogComponent as Component, params['id']);
            } else {
                this.advertiserRequirementPopupService
                    .open(AdvertiserRequirementDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
