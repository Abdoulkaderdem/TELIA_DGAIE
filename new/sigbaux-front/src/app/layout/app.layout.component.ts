import {
    ChangeDetectorRef,
    Component,
    OnDestroy,
    Renderer2,
    ViewChild,
} from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { MenuService } from './app.menu.service';
import { AppSidebarComponent } from './app.sidebar.component';
import { AppTopbarComponent } from './app.topbar.component';
import { LayoutService } from './service/app.layout.service';

@Component({
    selector: 'app-layout',
    templateUrl: './app.layout.component.html',
    styleUrls: ['./app.layout.component.css']
})
export class AppLayoutComponent implements OnDestroy {
    overlayMenuOpenSubscription: Subscription;

    menuOutsideClickListener: any;

    menuScrollListener: any;

    @ViewChild(AppSidebarComponent) appSidebar!: AppSidebarComponent;

    @ViewChild(AppTopbarComponent) appTopbar!: AppTopbarComponent;

    constructor(
        private menuService: MenuService,
        public layoutService: LayoutService,
        public renderer: Renderer2,
        public router: Router,
        private cd: ChangeDetectorRef
    ) {
        this.overlayMenuOpenSubscription =
            this.layoutService.overlayOpen$.subscribe(() => {
                if (!this.menuOutsideClickListener) {
                    this.menuOutsideClickListener = this.renderer.listen(
                        'document',
                        'click',
                        (event) => {
                            const isOutsideClicked = !(
                                this.appSidebar.el.nativeElement.isSameNode(
                                    event.target
                                ) ||
                                this.appSidebar.el.nativeElement.contains(
                                    event.target
                                ) ||
                                this.appTopbar.menuButton?.nativeElement.isSameNode(
                                    event.target
                                ) ||
                                this.appTopbar.menuButton?.nativeElement.contains(
                                    event.target
                                ) ||
                                this.appTopbar.mobileMenuButton?.nativeElement.isSameNode(
                                    event.target
                                ) ||
                                this.appTopbar.mobileMenuButton?.nativeElement.contains(
                                    event.target
                                )
                            );
                            if (isOutsideClicked) {
                                this.hideMenu();
                            }
                        }
                    );
                }

                if (
                    this.layoutService.isHorizontal() &&
                    !this.menuScrollListener
                ) {
                    this.menuScrollListener = this.renderer.listen(
                        this.appSidebar.menuContainer.nativeElement,
                        'scroll',
                        (event) => {
                            if (this.layoutService.isDesktop()) {
                                this.hideMenu();
                            }
                        }
                    );
                }

                if (this.layoutService.state.staticMenuMobileActive) {
                    this.blockBodyScroll();
                }
            });
    }

    blockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.add('blocked-scroll');
        } else {
            document.body.className += ' blocked-scroll';
        }
    }

    unblockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.remove('blocked-scroll');
        } else {
            document.body.className = document.body.className.replace(
                new RegExp(
                    '(^|\\b)' +
                        'blocked-scroll'.split(' ').join('|') +
                        '(\\b|$)',
                    'gi'
                ),
                ' '
            );
        }
    }

    hideMenu() {
        this.layoutService.state.overlayMenuActive = false;
        this.layoutService.state.staticMenuMobileActive = false;
        this.layoutService.state.menuHoverActive = false;
        this.menuService.reset();

        if (this.menuOutsideClickListener) {
            this.menuOutsideClickListener();
            this.menuOutsideClickListener = null;
        }

        if (this.menuScrollListener) {
            this.menuScrollListener();
            this.menuScrollListener = null;
        }
        this.unblockBodyScroll();
    }

    get containerClass() {
        let styleClass: { [key: string]: any } = {
            'layout-dark': this.layoutService.config().colorScheme === 'dark',
            'layout-dim': this.layoutService.config().colorScheme === 'dim',
            'layout-light': this.layoutService.config().colorScheme === 'light',
            'layout-overlay':
                this.layoutService.config().menuMode === 'overlay',
            'layout-static': this.layoutService.config().menuMode === 'static',
            'layout-horizontal':
                this.layoutService.config().menuMode === 'horizontal',
            'p-input-filled':
                this.layoutService.config().inputStyle === 'filled',
            'p-ripple-disabled': !this.layoutService.config().ripple,
            'layout-static-inactive':
                this.layoutService.state.staticMenuDesktopInactive &&
                this.layoutService.config().menuMode === 'static',
            'layout-overlay-active': this.layoutService.state.overlayMenuActive,
            'layout-mobile-active':
                this.layoutService.state.staticMenuMobileActive,
        };

        styleClass['layout-topbar-' + this.layoutService.config().topbarTheme] =
            true;
        styleClass['layout-menu-' + this.layoutService.config().menuTheme] =
            true;
        return styleClass;
    }

    ngOnDestroy() {
        if (this.overlayMenuOpenSubscription) {
            this.overlayMenuOpenSubscription.unsubscribe();
        }

        if (this.menuOutsideClickListener) {
            this.menuOutsideClickListener();
        }
    }
}
