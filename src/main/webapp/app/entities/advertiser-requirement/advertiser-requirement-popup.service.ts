import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AdvertiserRequirement } from './advertiser-requirement.model';
import { AdvertiserRequirementService } from './advertiser-requirement.service';

@Injectable()
export class AdvertiserRequirementPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private advertiserRequirementService: AdvertiserRequirementService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.advertiserRequirementService.find(id).subscribe((advertiserRequirement) => {
                    this.ngbModalRef = this.advertiserRequirementModalRef(component, advertiserRequirement);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.advertiserRequirementModalRef(component, new AdvertiserRequirement());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    advertiserRequirementModalRef(component: Component, advertiserRequirement: AdvertiserRequirement): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.advertiserRequirement = advertiserRequirement;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
