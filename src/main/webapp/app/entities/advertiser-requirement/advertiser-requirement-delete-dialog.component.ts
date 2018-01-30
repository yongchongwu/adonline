import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AdvertiserRequirement } from './advertiser-requirement.model';
import { AdvertiserRequirementPopupService } from './advertiser-requirement-popup.service';
import { AdvertiserRequirementService } from './advertiser-requirement.service';

@Component({
    selector: 'jhi-advertiser-requirement-delete-dialog',
    templateUrl: './advertiser-requirement-delete-dialog.component.html'
})
export class AdvertiserRequirementDeleteDialogComponent {

    advertiserRequirement: AdvertiserRequirement;

    constructor(
        private advertiserRequirementService: AdvertiserRequirementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.advertiserRequirementService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'advertiserRequirementListModification',
                content: 'Deleted an advertiserRequirement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-advertiser-requirement-delete-popup',
    template: ''
})
export class AdvertiserRequirementDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private advertiserRequirementPopupService: AdvertiserRequirementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.advertiserRequirementPopupService
                .open(AdvertiserRequirementDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
